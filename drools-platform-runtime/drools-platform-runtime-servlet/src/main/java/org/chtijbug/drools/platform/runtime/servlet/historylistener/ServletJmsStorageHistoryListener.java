/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.runtime.servlet.historylistener;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.core.droolslistener.PlatformHistoryListener;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.platform.runtime.servlet.DroolsPlatformKnowledgeBaseJavaEE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;


@Component
public class ServletJmsStorageHistoryListener implements PlatformHistoryListener {

    private String platformQueueName = "historyEventQueue";
    private Integer platformPort = 61616;
    private String platformServer;

    private static final Logger LOG = Logger.getLogger(ServletJmsStorageHistoryListener.class);

    private Integer ruleBaseID;

    private MessageProducer producer;

    private Session session;

    private boolean jmsConnected = false;

    final BlockingQueue<HistoryEvent> cachedHistoryEvents = new LinkedBlockingDeque<HistoryEvent>();

    @Value(value = "${knowledge.numberRetriesConnectionToRuntime}")
    private String numberRetriesString;


    @Value(value = "${knowledge.timeToWaitBetweenTwoRetries}")
    private String timeToWaitBetweenTwoRetriesString;

    Semaphore jmsConnectedSemaphore = new Semaphore(1);

    DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE;


    public void initJmsConnection() throws DroolsChtijbugException {

        int numberRetries = new Integer(this.numberRetriesString);
        int timeToWaitBetweenTwoRetries = new Integer(this.timeToWaitBetweenTwoRetriesString);
        String url = "tcp://" + this.platformServer + ":" + this.platformPort;
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        Connection connection = null;
        JMSConnectionListener jmsConnectionListener = new JMSConnectionListener() {
            @Override
            public void connected(Connection connection) throws JMSException, InterruptedException {
                try {
                    session = connection.createSession(false,
                            Session.AUTO_ACKNOWLEDGE);
                    Queue queue = session.createQueue(platformQueueName);
                    producer = session.createProducer(queue);
                    jmsConnectedSemaphore.acquire();
                    for (HistoryEvent cachedhistoryEvent : cachedHistoryEvents) {
                        ObjectMessage cachedmsg = session.createObjectMessage(cachedhistoryEvent);
                        producer.send(cachedmsg);
                    }
                    cachedHistoryEvents.clear();
                    jmsConnected = true;
                    jmsConnectedSemaphore.release();
                } catch (JMSException e) {
                    throw e;
                } catch (InterruptedException e) {
                    throw e;
                }

            }
        };
        CreateJMSConnectionThread createJMSConnectionThread = new CreateJMSConnectionThread(jmsConnectionListener, numberRetries, factory, timeToWaitBetweenTwoRetries);
        createJMSConnectionThread.start();
    }


    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {
        try {
            jmsConnectedSemaphore.acquire();
        } catch (InterruptedException e) {
            DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("ServletJmsStorageHistoryListener.fireEvent", "Acquire not possible", e);
            throw droolsChtijbugException;
        }
        if (jmsConnected == false) {
            /**
             * If no connection is possible, cache the history Event
             */
            cachedHistoryEvents.add(historyEvent);
            jmsConnectedSemaphore.release();
        } else {
            try {
                /**
                 * If some history events were cached before, send them first
                 */
                for (HistoryEvent cachedhistoryEvent : cachedHistoryEvents) {
                    ObjectMessage cachedmsg = session.createObjectMessage(cachedhistoryEvent);
                    producer.send(cachedmsg);
                }
                cachedHistoryEvents.clear();
                ObjectMessage msg = session.createObjectMessage(historyEvent);
                producer.send(msg);
            } catch (JMSException e) {
                DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("JMSHistoryEvent", "FireEvent", e);
                throw droolsChtijbugException;
            } finally {
                jmsConnectedSemaphore.release();
            }
        }


    }

    public void shutdown() {
        final PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = new PlatformKnowledgeBaseShutdownEvent(-1, new Date(), Integer.valueOf(this.ruleBaseID).intValue(), new Date());

        try {
            this.fireEvent(platformKnowledgeBaseShutdownEvent);
            session.close();
        } catch (JMSException e) {
            LOG.error("Session Could not be closed", e);
        } catch (DroolsChtijbugException e) {
            LOG.error("Session Could not be closed", e);
        }

    }

    @Override
    protected void finalize()
            throws Throwable {
        super.finalize();

    }

    public void setPlatformKnowledgeBaseJavaEE(DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE) {
        this.platformKnowledgeBaseJavaEE = platformKnowledgeBaseJavaEE;
    }

    public void initJMSConnection() throws DroolsChtijbugException {
        this.ruleBaseID = this.platformKnowledgeBaseJavaEE.getRuleBaseID();
        this.platformServer = this.platformKnowledgeBaseJavaEE.getPlatformServer();
        this.platformKnowledgeBaseJavaEE.setServletJmsStorageHistoryListener(this);
        this.initJmsConnection();
        this.platformKnowledgeBaseJavaEE.startConnectionToPlatform();
    }
}
