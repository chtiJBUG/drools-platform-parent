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

import java.util.List;

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
        List<PlatformRuntime> existingPlatformRuntimes = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseDisposeEvent.getRuleBaseID());
        if (existingPlatformRuntimes.size()==1) {
            existingPlatformRuntimes.get(0).setEndDate(knowledgeBaseDisposeEvent.getDateEvent());
            existingPlatformRuntimes.get(0).setShutdowDate(knowledgeBaseDisposeEvent.getDateEvent());
            platformRuntimeRepository.save( existingPlatformRuntimes.get(0));
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseDisposeEvent;
    }
}
