package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.DeletedFactHistoryEvent;
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
public class DeleteFactEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(DeleteFactEventStrategy.class);

    @Autowired
    RuleExecutionRepositoryCacheService ruleExecutionRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        DeletedFactHistoryEvent deletedFactHistoryEvent = (DeletedFactHistoryEvent) historyEvent;
        Fact fact = new Fact();
        fact.setFullClassName(deletedFactHistoryEvent.getDeletedObject().getFullClassName());
        fact.setObjectVersion(deletedFactHistoryEvent.getDeletedObject().getObjectVersion());
        fact.setJsonFact(deletedFactHistoryEvent.getDeletedObject().getRealObject_JSON());
        fact.setModificationDate(deletedFactHistoryEvent.getDateEvent());
        fact.setFactType(FactType.INSERTED);
        RuleExecution existingInSessionRuleExecution = null;
        if (deletedFactHistoryEvent.getRuleName() == null) {  // inserted from a session
            SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId());
            sessionExecution.getFacts().add(fact);
            sessionExecutionRepository.save(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId(), sessionExecution);

        } else if (deletedFactHistoryEvent.getRuleName() != null && deletedFactHistoryEvent.getRuleflowGroup() == null) {   // inserted from a rule that is not in a ruleflow/process
            existingInSessionRuleExecution = ruleExecutionRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId(), deletedFactHistoryEvent.getRuleName());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(fact);
                ruleExecutionRepository.save(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId(), deletedFactHistoryEvent.getRuleflowGroup(), existingInSessionRuleExecution);
            }

        } else { // inserted from a rule in a ruleflowgroup/process
            existingInSessionRuleExecution = ruleExecutionRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId(), deletedFactHistoryEvent.getRuleflowGroup(), deletedFactHistoryEvent.getRuleName());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(fact);
                ruleExecutionRepository.save(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId(), deletedFactHistoryEvent.getRuleflowGroup(), existingInSessionRuleExecution);
            }
        }


        LOG.debug("DeletedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof DeletedFactHistoryEvent;
    }
}
