package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesBeginEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.FireAllRulesExecutionRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecution;
import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecutionStatus;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
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
public class KnowledgeSessionFireAllRulesBeginEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesBeginEventStrategy.class);

    @Autowired
    FireAllRulesExecutionRepository fireAllRulesExecutionRepository;

    @Autowired
    SessionExecutionRepository sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesBeginEvent sessionFireAllRulesBeginEvent = (SessionFireAllRulesBeginEvent) historyEvent;

        List<FireAllRulesExecution> fireAllRulesExecutions = fireAllRulesExecutionRepository.findAllStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        for (FireAllRulesExecution runningFireAllRulesExecution : fireAllRulesExecutions) {
            runningFireAllRulesExecution.setEndDate(new Date());
            runningFireAllRulesExecution.setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus.CRASHED);
            fireAllRulesExecutionRepository.save(runningFireAllRulesExecution);
        }
        SessionExecution existingSessionRutime = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());

        FireAllRulesExecution fireAllRulesExecution = new FireAllRulesExecution();
        fireAllRulesExecution.setSessionExecution(existingSessionRutime);
        fireAllRulesExecution.setStartEventID(sessionFireAllRulesBeginEvent.getEventID());
        fireAllRulesExecution.setStartDate(sessionFireAllRulesBeginEvent.getDateEvent());
        fireAllRulesExecution.setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus.STARTED);
        fireAllRulesExecutionRepository.save(fireAllRulesExecution);
        LOG.debug("SessionFireAllRulesBeginEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesBeginEvent;
    }
}
