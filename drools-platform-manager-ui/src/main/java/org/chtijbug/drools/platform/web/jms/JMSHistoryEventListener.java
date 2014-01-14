package org.chtijbug.drools.platform.web.jms;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.web.service.KnowledgeBaseService;
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
    KnowledgeBaseService knowledgeBaseService;



    public void onMessage(Message message) {
        try {
            if (message instanceof PlatformKnowledgeBaseCreatedEvent) {
                PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent = (PlatformKnowledgeBaseCreatedEvent)message;
                knowledgeBaseService.handleMessage(platformKnowledgeBaseCreatedEvent);
            }

            ObjectMessage msg = (ObjectMessage) message;
            LOG.info("Consumed message: " + msg.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}