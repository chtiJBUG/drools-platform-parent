package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.InsertedFactHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.Fact;
import org.chtijbug.drools.platform.persistence.pojo.FactType;
import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
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
public class InsertedFactEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(InsertedFactEventStrategy.class);

    @Autowired
    RuleExecutionRepositoryCacheService ruleExecutionRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        InsertedFactHistoryEvent insertedFactHistoryEvent = (InsertedFactHistoryEvent) historyEvent;
        Fact fact = new Fact();
        fact.setFullClassName(insertedFactHistoryEvent.getInsertedObject().getFullClassName());
        fact.setObjectVersion(insertedFactHistoryEvent.getInsertedObject().getObjectVersion());
        fact.setJsonFact(insertedFactHistoryEvent.getInsertedObject().getRealObject_JSON());
        fact.setModificationDate(insertedFactHistoryEvent.getDateEvent());
        fact.setFactType(FactType.INSERTED);
        RuleExecution existingInSessionRuleExecution = null;
        if (insertedFactHistoryEvent.getRuleName() == null) {  // inserted from a session
            SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
            sessionExecution.getFacts().add(fact);
            sessionExecutionRepository.save(sessionExecution);

        } else if (insertedFactHistoryEvent.getRuleName() != null && insertedFactHistoryEvent.getRuleflowGroup() == null) {   // inserted from a rule that is not in a ruleflow/process
            existingInSessionRuleExecution = ruleExecutionRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId(), insertedFactHistoryEvent.getRuleName());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(fact);
                ruleExecutionRepository.save(existingInSessionRuleExecution);
            }

        } else { // inserted from a rule in a ruleflowgroup/process
            existingInSessionRuleExecution = ruleExecutionRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId(), insertedFactHistoryEvent.getRuleflowGroup(), insertedFactHistoryEvent.getRuleName());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(fact);
                ruleExecutionRepository.save(existingInSessionRuleExecution);
            }
        }


        LOG.debug("InsertedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof InsertedFactHistoryEvent;
    }
}
