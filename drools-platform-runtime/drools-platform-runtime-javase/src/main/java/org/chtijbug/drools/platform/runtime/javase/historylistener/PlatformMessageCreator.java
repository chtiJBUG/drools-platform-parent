package org.chtijbug.drools.platform.runtime.javase.historylistener;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * Created by nheron on 05/06/15.
 */
public class PlatformMessageCreator implements MessageCreator {


    private HistoryEvent eventToSent=null;

    public PlatformMessageCreator(HistoryEvent eventToSent) {
        this.eventToSent = eventToSent;
    }

    @Override
    public Message createMessage(Session session) throws JMSException {
        ObjectMessage message = session.createObjectMessage(eventToSent);
        return message;
    }
}
