package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by nheron on 07/06/15.
 */
@Component
public class PlatformKnowledgeBaseDisposeSessionEventStrategy extends AbstractEventHandlerStrategy {

    @Autowired
    MessageHandlerResolver messageHandlerResolver;

   @Override
   @Transactional(value = "transactionManager")
    protected void handleMessageInternally(HistoryEvent historyEvent) {
       //TODO
        PlatformKnowledgeBaseDisposeSessionEvent event=(PlatformKnowledgeBaseDisposeSessionEvent)historyEvent;
      //  for (HistoryEvent historyEventElement : event.getSessionHistory()){
      //      AbstractEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandler(historyEventElement);
      //      strategy.handleMessage(historyEventElement);
      //  }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseDisposeSessionEvent;
    }

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        return true;
    }
}
