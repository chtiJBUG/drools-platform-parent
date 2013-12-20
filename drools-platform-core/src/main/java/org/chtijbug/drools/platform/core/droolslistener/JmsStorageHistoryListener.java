package org.chtijbug.drools.platform.core.droolslistener;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/12/13
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public class JmsStorageHistoryListener implements HistoryListener {

    @Autowired
    private JmsTemplate jmsTemplate ;
    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {
        final Serializable objectToSend = historyEvent;
             jmsTemplate.send(new MessageCreator() {

                public Message createMessage(Session session) throws JMSException {
                     ObjectMessage message = session.createObjectMessage();
                     message.setObject(objectToSend);
                     return message;
                  }

            });

        }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
