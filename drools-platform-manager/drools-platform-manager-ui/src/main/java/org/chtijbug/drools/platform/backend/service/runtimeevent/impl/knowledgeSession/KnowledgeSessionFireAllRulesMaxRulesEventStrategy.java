package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesMaxNumberReachedEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.FireAllRulesExecutionRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecution;
import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecutionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionFireAllRulesMaxRulesEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesMaxRulesEventStrategy.class);

    @Autowired
    FireAllRulesExecutionRepository fireAllRulesExecutionRepository;

    @Autowired
    SessionExecutionRepository sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesMaxNumberReachedEvent sessionFireAllRulesMaxNumberReachedEvent = (SessionFireAllRulesMaxNumberReachedEvent) historyEvent;
        FireAllRulesExecution fireAllRulesExecution = fireAllRulesExecutionRepository.findStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        fireAllRulesExecution.setMaxRulesEventID(sessionFireAllRulesMaxNumberReachedEvent.getEventID());
        fireAllRulesExecution.setEndDate(sessionFireAllRulesMaxNumberReachedEvent.getDateEvent());
        fireAllRulesExecution.setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus.MAXNBRULES);
        fireAllRulesExecution.setNbreRulesFired(Long.valueOf(sessionFireAllRulesMaxNumberReachedEvent.getNumberOfRulesExecuted()));
        fireAllRulesExecution.setMaxNbreRulesDefinedForSession(Long.valueOf(sessionFireAllRulesMaxNumberReachedEvent.getMaxNumberOfRulesForSession()));
        fireAllRulesExecutionRepository.save(fireAllRulesExecution);
        LOG.debug("SessionFireAllRulesMaxNumberReachedEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesMaxNumberReachedEvent;
    }
}
