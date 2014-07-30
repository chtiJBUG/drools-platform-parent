package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionCreatedEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecutionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionCreateEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionCreateEventStrategy.class);

    @Autowired
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionCreatedEvent sessionCreatedEvent = (SessionCreatedEvent) historyEvent;
        SessionExecution existingSessionRutime = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());
        if (existingSessionRutime != null) {
            existingSessionRutime.setEndDate(new Date());
            existingSessionRutime.setSessionExecutionStatus(SessionExecutionStatus.CRASHED);
            sessionExecutionRepository.save(existingSessionRutime);
        }
        List<PlatformRuntimeInstance> platformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(sessionCreatedEvent.getRuleBaseID());
        if (platformRuntimeInstances.size() == 1) {
            SessionExecution sessionExecution = new SessionExecution();
            sessionExecution.setPlatformRuntimeInstance(platformRuntimeInstances.get(0));
            sessionExecution.setStartDate(sessionCreatedEvent.getDateEvent());
            sessionExecution.setSessionId(sessionCreatedEvent.getSessionId());
            sessionExecution.setStartEventID(sessionCreatedEvent.getEventID());
            sessionExecution.setSessionExecutionStatus(SessionExecutionStatus.STARTED);
            sessionExecutionRepository.save(sessionExecution);
        }
        LOG.debug("SessionCreatedEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionCreatedEvent;
    }
}
