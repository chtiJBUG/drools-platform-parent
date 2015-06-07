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
package org.chtijbug.drools.platform.runtime.javase.historylistener;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBaseRuntime;
import org.chtijbug.drools.platform.core.droolslistener.PlatformHistoryListener;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.platform.runtime.javase.DroolsPlatformKnowledgeBaseJavaSE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Date;


public class JmsStorageHistoryListener implements PlatformHistoryListener {

    private String platformQueueName;
    private Integer platformPort;
    private String platformServer;

    private static final Logger LOG = Logger.getLogger(JmsStorageHistoryListener.class);

    private Integer ruleBaseID;


    private DroolsPlatformKnowledgeBaseRuntime droolsPlatformKnowledgeBase;
    private JmsTemplate jmsTemplate;

    public JmsStorageHistoryListener(DroolsPlatformKnowledgeBaseRuntime droolsPlatformKnowledgeBase, String platformServer, Integer platformPort, String platformQueueName) throws DroolsChtijbugException {
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;
        this.platformServer = platformServer;
        this.platformPort = platformPort;
        this.platformQueueName = platformQueueName;
        this.ruleBaseID = this.droolsPlatformKnowledgeBase.getRuleBaseID();
        try {
            initJmsConnection();
        } catch (JMSException e) {
            DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("JmsStorageHistoryListener", "Could not initialize JMS Connection", e);
            throw droolsChtijbugException;
        }

    }

    private void initJmsConnection() throws JMSException {
        String url = "tcp://" + this.platformServer + ":" + this.platformPort;
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        CachingConnectionFactory cacheFactory = new CachingConnectionFactory(factory);
        this.jmsTemplate = new JmsTemplate(cacheFactory);
    }

    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {
        this.jmsTemplate.send(platformQueueName, new PlatformMessageCreator(historyEvent));
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


    public void setDroolsPlatformKnowledgeBase(DroolsPlatformKnowledgeBaseJavaSE droolsPlatformKnowledgeBaseJavaSE) throws UnknownHostException {
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBaseJavaSE;


    }
}
