package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
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
public class PlatformKnowledgeBaseShutdownEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(PlatformKnowledgeBaseShutdownEventStrategy.class);


    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = (PlatformKnowledgeBaseShutdownEvent) historyEvent;
        List<PlatformRuntime> existingPlatformRuntimes = null;
        try {
            existingPlatformRuntimes = platformRuntimeRepository.findByRuleBaseIDAndShutdowDateNull(platformKnowledgeBaseShutdownEvent.getRuleBaseID());
            if (existingPlatformRuntimes.size()==1) {
                existingPlatformRuntimes.get(0).setShutdowDate(platformKnowledgeBaseShutdownEvent.getDateEvent());
                platformRuntimeRepository.save(existingPlatformRuntimes.get(0));
            }
        } catch (Exception e) {
            LOG.error(platformKnowledgeBaseShutdownEvent, e);
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseShutdownEvent;
    }
}
