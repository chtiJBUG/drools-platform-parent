package org.chtijbug.drools.platform.backend.jms;

import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.MessageHandlerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


/**
 * Created by IntelliJ IDEA.
 * Date: 20/12/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
@Component("simpleMessageListener")
public class JMSHistoryEventListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(JMSHistoryEventListener.class);

    @Autowired
    MessageHandlerResolver messageHandlerResolver;
    @Transactional
    public void onMessage(Message message) {
        HistoryEvent historyEvent=null;
        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Object messageContent = objectMessage.getObject();
                historyEvent=(HistoryEvent)messageContent;
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
            LOG.error("Consumed message: " + e.toString()+" message content " +message.toString());
            LOG.error("                  message  " +message.toString());
            LOG.error("                  object content " + historyEvent.toString());

        }
    }

}