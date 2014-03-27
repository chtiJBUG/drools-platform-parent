package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreateSessionEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseCreateSessionEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseCreateSessionEventStrategy.class);
    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        LOG.info("KnowledgeBaseCreateSessionEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof KnowledgeBaseCreateSessionEvent;
    }
}
