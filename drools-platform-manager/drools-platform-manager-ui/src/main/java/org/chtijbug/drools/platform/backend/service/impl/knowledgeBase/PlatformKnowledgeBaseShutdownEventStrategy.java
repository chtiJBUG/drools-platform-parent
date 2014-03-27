package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = (PlatformKnowledgeBaseShutdownEvent) historyEvent;
        PlatformRuntime existingPlatformRuntime = null;
        try {
            existingPlatformRuntime = platformRuntimeRepository.findByRuleBaseIDAndShutdowDateNull(platformKnowledgeBaseShutdownEvent.getRuleBaseID());
            existingPlatformRuntime.setShutdowDate(platformKnowledgeBaseShutdownEvent.getDateEvent());
            platformRuntimeRepository.save(existingPlatformRuntime);
        } catch (Exception e) {
            LOG.error(platformKnowledgeBaseShutdownEvent, e);
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseShutdownEvent;
    }
}
