package org.chtijbug.drools.platform.backend.jms;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.platform.backend.service.KnowledgeBaseService;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
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
            if (message instanceof ObjectMessage) {
                ObjectMessage objectMessage = (ObjectMessage) message;
                Object messageContent = objectMessage.getObject();
                if (messageContent instanceof PlatformKnowledgeBaseCreatedEvent) {
                    PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent = (PlatformKnowledgeBaseCreatedEvent) messageContent;
                    knowledgeBaseService.handleMessage(platformKnowledgeBaseCreatedEvent);
                } else if (messageContent instanceof KnowledgeBaseInitialLoadEvent) {
                    KnowledgeBaseInitialLoadEvent knowledgeBaseInitialLoadEvent = (KnowledgeBaseInitialLoadEvent) messageContent;
                    knowledgeBaseService.handleMessage(knowledgeBaseInitialLoadEvent);
                }  else if (messageContent instanceof PlatformKnowledgeBaseShutdownEvent){
                    PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = (PlatformKnowledgeBaseShutdownEvent)messageContent;
                    knowledgeBaseService.handleMessage(platformKnowledgeBaseShutdownEvent);
                }  else if (messageContent instanceof KnowledgeBaseAddRessourceEvent){
                    KnowledgeBaseAddRessourceEvent knowledgeBaseAddRessourceEvent = (KnowledgeBaseAddRessourceEvent)messageContent;
                    knowledgeBaseService.handleMessage(knowledgeBaseAddRessourceEvent);

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