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
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;


@Component
public class ServletJmsStorageHistoryListener implements PlatformHistoryListener {

    private String platformQueueName = "historyEventQueue";
    private Integer jmsPort = 61616;
    private String jmsServer;

    private static final Logger LOG = Logger.getLogger(ServletJmsStorageHistoryListener.class);

    private Integer ruleBaseID;

    private JmsTemplate jmsTemplate;


    private boolean jmsConnected = false;

    final BlockingQueue<HistoryEvent> cachedHistoryEvents = new LinkedBlockingDeque<HistoryEvent>();

    @Value(value = "${knowledge.numberRetriesConnectionToRuntime}")
    private String numberRetriesString;


    @Value(value = "${knowledge.timeToWaitBetweenTwoRetries}")
    private String timeToWaitBetweenTwoRetriesString;

    Semaphore jmsConnectedSemaphore = new Semaphore(1);

    DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE;


    public void initJmsConnection() throws DroolsChtijbugException {


        String url = "tcp://" + this.jmsServer + ":" + this.jmsPort;
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        CachingConnectionFactory cacheFactory = new CachingConnectionFactory(factory);
        this.jmsTemplate = new JmsTemplate(cacheFactory);
        this.jmsConnected=true;

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

            LOG.debug("Storing to cache event" + historyEvent.toString());
            jmsConnectedSemaphore.release();

        } else {
            try {
                /**
                 * If some history events were cached before, send them first
                 */
                for (HistoryEvent cachedhistoryEvent : cachedHistoryEvents) {
                    this.jmsTemplate.send(platformQueueName, new PlatformMessageCreator(cachedhistoryEvent));
                    LOG.debug("Sending Cached Event" + cachedhistoryEvent.toString());
                }
                cachedHistoryEvents.clear();
                this.jmsTemplate.send(platformQueueName, new PlatformMessageCreator(historyEvent));
                LOG.debug("Sending JMS Event" + historyEvent.toString());
            } finally {
                jmsConnectedSemaphore.release();
            }
        }


    }

    public void shutdown() {
        final PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = new PlatformKnowledgeBaseShutdownEvent(-1, new Date(), Integer.valueOf(this.ruleBaseID).intValue(), new Date());

        try {
            this.fireEvent(platformKnowledgeBaseShutdownEvent);
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
        LOG.debug("<<ServletJmsStorageHistoryListener");
        this.ruleBaseID = this.platformKnowledgeBaseJavaEE.getRuleBaseID();
        this.jmsServer = this.platformKnowledgeBaseJavaEE.getJmsServer();
        this.jmsPort = this.platformKnowledgeBaseJavaEE.getJmsPort();
        this.platformKnowledgeBaseJavaEE.setServletJmsStorageHistoryListener(this);
        this.initJmsConnection();
        this.platformKnowledgeBaseJavaEE.startConnectionToPlatform();
        LOG.debug(">>ServletJmsStorageHistoryListener");
    }
}
