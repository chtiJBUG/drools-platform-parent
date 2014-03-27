package org.chtijbug.drools.platform.backend.jms;

import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.MessageHandlerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void onMessage(Message message) {

        try {
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Object messageContent = objectMessage.getObject();
                HistoryEvent historyEvent=(HistoryEvent)messageContent;
                try {
                    AbstractEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandler(historyEvent);
                    strategy.handleMessage(historyEvent);
                   // handleKnowledgeEventService.getClass().getMethod("handleMessage",messageContent.getClass()).invoke(handleKnowledgeEventService,messageContent) ;
                  } catch (Throwable e) {
                      throw Throwables.propagate(e);
                  }
                ObjectMessage msg = (ObjectMessage) message;
                LOG.info("Consumed message: " + msg.toString());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}