package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseDisposeEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseDisposeEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseDisposeEventStrategy.class);


    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseDisposeEvent knowledgeBaseDisposeEvent = (KnowledgeBaseDisposeEvent) historyEvent;
        PlatformRuntime existingPlatformRuntime = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseDisposeEvent.getRuleBaseID());
        existingPlatformRuntime.setEndDate(knowledgeBaseDisposeEvent.getDateEvent());
        platformRuntimeRepository.save(existingPlatformRuntime);
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseDisposeEvent;
    }
}
