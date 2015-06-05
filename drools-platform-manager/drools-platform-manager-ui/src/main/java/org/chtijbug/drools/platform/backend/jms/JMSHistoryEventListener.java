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
package org.chtijbug.drools.platform.backend.jms;

import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


@Component("simpleMessageListener")
public class JMSHistoryEventListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(JMSHistoryEventListener.class);

    @Autowired
    MessageHandlerResolver messageHandlerResolver;

    @Transactional
    public void onMessage(Message message) {
        HistoryEvent historyEvent = null;
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Object messageContent = objectMessage.getObject();
                historyEvent = (HistoryEvent) messageContent;
                try {
                    AbstractEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandler(historyEvent);
                    strategy.handleMessage(historyEvent);
                } catch (Throwable e) {
                    throw Throwables.propagate(e);
                }
                ObjectMessage msg = (ObjectMessage) message;
                LOG.debug("Consumed message: " + msg.toString());
            }
        } catch (Exception e) {
            LOG.error("Consumed message: " + e.toString() + " message content " + message.toString());
            LOG.error("                  message  " + message.toString());
            LOG.error("                  object content " + historyEvent.toString());

        }
    }

}