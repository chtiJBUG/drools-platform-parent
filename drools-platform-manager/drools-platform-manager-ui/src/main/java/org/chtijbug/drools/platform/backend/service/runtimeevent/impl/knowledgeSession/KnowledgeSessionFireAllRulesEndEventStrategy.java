package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesEndEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.FireAllRulesExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
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
public class KnowledgeSessionFireAllRulesEndEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesEndEventStrategy.class);

    @Autowired
    FireAllRulesExecutionRepositoryCacheService fireAllRulesExecutionRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesEndEvent sessionFireAllRulesEndEvent = (SessionFireAllRulesEndEvent) historyEvent;
        FireAllRulesExecution fireAllRulesExecution = fireAllRulesExecutionRepository.findStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        fireAllRulesExecution.setStopEventID(sessionFireAllRulesEndEvent.getEventID());
        fireAllRulesExecution.setEndDate(sessionFireAllRulesEndEvent.getDateEvent());
        fireAllRulesExecution.setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus.STOPPED);
        fireAllRulesExecution.setNbreRulesFired(Long.valueOf(sessionFireAllRulesEndEvent.getNumberRulesExecuted()));
        fireAllRulesExecution.setExecutionTime(Long.valueOf(sessionFireAllRulesEndEvent.getExecutionTime()));
        fireAllRulesExecutionRepository.save(fireAllRulesExecution);
        LOG.debug("SessionFireAllRulesEndEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesEndEvent;
    }
}
