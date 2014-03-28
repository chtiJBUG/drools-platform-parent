package org.chtijbug.drools.platform.rules;

import com.google.common.base.Throwables;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.MessageHandlerResolver;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/02/14
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DirectAccessHistoryListener implements HistoryListener {
    @Autowired
    MessageHandlerResolver messageHandlerResolver;

    @Override
    public void fireEvent(HistoryEvent newHistoryEvent) throws DroolsChtijbugException {
        HistoryEvent historyEventToSend = newHistoryEvent;
        historyEventToSend.setRuleBaseID(1);
        if (newHistoryEvent instanceof KnowledgeBaseCreatedEvent) {
            PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent = new PlatformKnowledgeBaseCreatedEvent(newHistoryEvent.getEventID(), newHistoryEvent.getDateEvent(), newHistoryEvent.getRuleBaseID(), "localhost", 8025, new Date());
            historyEventToSend = platformKnowledgeBaseCreatedEvent;
        }
        try {
            AbstractEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandler(historyEventToSend);
            strategy.handleMessage(historyEventToSend);
        } catch (Throwable e) {
            throw Throwables.propagate(e);
        }
    }
}
