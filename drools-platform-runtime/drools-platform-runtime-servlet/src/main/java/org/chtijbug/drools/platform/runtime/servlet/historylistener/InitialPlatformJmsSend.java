package org.chtijbug.drools.platform.runtime.servlet.historylistener;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.springframework.jms.core.JmsTemplate;

import java.util.concurrent.BlockingQueue;

/**
 * Created by nheron on 18/06/15.
 */
public class InitialPlatformJmsSend implements Runnable {

    private static final Logger LOG = Logger.getLogger(InitialPlatformJmsSend.class);
    BlockingQueue<HistoryEvent> cachedHistoryEvents;
    private JmsTemplate jmsTemplate;
    private PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent;
    private String platformQueueName;

    public InitialPlatformJmsSend(JmsTemplate jmsTemplate, PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent, String platformQueueName, BlockingQueue<HistoryEvent> cachedHistoryEvents) {
        this.jmsTemplate = jmsTemplate;
        this.platformKnowledgeBaseInitialConnectionEvent = platformKnowledgeBaseInitialConnectionEvent;
        this.platformQueueName = platformQueueName;
        this.cachedHistoryEvents = cachedHistoryEvents;
    }

    @Override
    public void run() {
        boolean isNotSent = true;

        while (isNotSent) {
            try {
                this.jmsTemplate.send(platformQueueName, new PlatformMessageCreator(platformKnowledgeBaseInitialConnectionEvent));
                isNotSent = false;
                for (HistoryEvent cachedhistoryEvent : cachedHistoryEvents) {
                    this.jmsTemplate.send(platformQueueName, new PlatformMessageCreator(cachedhistoryEvent));
                    LOG.debug("Sending Cached Event" + cachedhistoryEvent.toString());
                }
                cachedHistoryEvents.clear();
            } catch (Exception ee) {
                LOG.debug("Sending PlatformKnowledgeBaseInitialConnectionEvent Event failed" + ee.toString());
                try {
                    Thread.sleep(3000);
                } catch (Exception eee) {
                    LOG.debug("waiting to send  PlatformKnowledgeBaseInitialConnectionEvent Event failed" + eee.toString());
                }
            }
        }

    }
}
