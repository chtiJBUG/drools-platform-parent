package org.chtijbug.drools.platform.backend.service.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.UpdatedFactHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.Fact;
import org.chtijbug.drools.platform.persistence.pojo.FactType;
import org.chtijbug.drools.platform.persistence.pojo.RuleRuntime;
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
public class UpdatedFactEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(UpdatedFactEventStrategy.class);
    @Autowired
    RulesRuntimeRepository rulesRuntimeRepository;

    @Autowired
    SessionExecutionRepository sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        UpdatedFactHistoryEvent updatedFactHistoryEvent = (UpdatedFactHistoryEvent) historyEvent;
        Fact factOldValue = new Fact();
        factOldValue.setFullClassName(updatedFactHistoryEvent.getObjectOldValue().getFullClassName());
        factOldValue.setObjectVersion(updatedFactHistoryEvent.getObjectOldValue().getObjectVersion());
        factOldValue.setJsonFact(updatedFactHistoryEvent.getObjectOldValue().getRealObject_JSON());
        factOldValue.setModificationDate(updatedFactHistoryEvent.getDateEvent());
        factOldValue.setFactType(FactType.UPDATED_OLDVALUE);
        Fact factNewValue = new Fact();
        factNewValue.setFullClassName(updatedFactHistoryEvent.getObjectNewValue().getFullClassName());
        factNewValue.setObjectVersion(updatedFactHistoryEvent.getObjectNewValue().getObjectVersion());
        factNewValue.setJsonFact(updatedFactHistoryEvent.getObjectNewValue().getRealObject_JSON());
        factNewValue.setModificationDate(updatedFactHistoryEvent.getDateEvent());
        factNewValue.setFactType(FactType.UPDATED_NEWVALUE);
        RuleRuntime existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInSessionByRuleBaseIDAndSessionID(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
        if (existingInSessionRuleRuntime != null) {
            existingInSessionRuleRuntime.getThenFacts().add(factOldValue);
            existingInSessionRuleRuntime.getThenFacts().add(factNewValue);
            rulesRuntimeRepository.save(existingInSessionRuleRuntime);

        } else {
            existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
            if (existingInSessionRuleRuntime != null) {
                existingInSessionRuleRuntime.getThenFacts().add(factOldValue);
                existingInSessionRuleRuntime.getThenFacts().add(factNewValue);
                rulesRuntimeRepository.save(existingInSessionRuleRuntime);
            } else {
                SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
                sessionExecution.getFacts().add(factOldValue);
                sessionExecution.getFacts().add(factNewValue);
                sessionExecutionRepository.save(sessionExecution);
            }
        }
        LOG.debug("UpdatedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof UpdatedFactHistoryEvent;
    }
}
