package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.InsertedFactHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleExecutionRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
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
    RuleExecutionRepository ruleExecutionRepository;

    @Autowired
    SessionExecutionRepository sessionExecutionRepository;

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
        RuleExecution existingInSessionRuleExecution = ruleExecutionRepository.findActiveRuleInSessionByRuleBaseIDAndSessionID(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
        if (existingInSessionRuleExecution != null) {
            existingInSessionRuleExecution.getThenFacts().add(fact);
            ruleExecutionRepository.save(existingInSessionRuleExecution);

        } else {
            existingInSessionRuleExecution = ruleExecutionRepository.findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(fact);
                ruleExecutionRepository.save(existingInSessionRuleExecution);
            } else {
                SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
                sessionExecution.getFacts().add(fact);
                sessionExecutionRepository.save(sessionExecution);
            }
        }
        LOG.debug("InsertedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof InsertedFactHistoryEvent;
    }
}
