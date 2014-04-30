package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseCreatedEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseCreatedEventStrategy.class);

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {

    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseCreatedEvent;
    }
}
