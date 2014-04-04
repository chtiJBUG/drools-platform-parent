package org.chtijbug.drools.platform.backend.service.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.UpdatedFactHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.FactRuntime;
import org.chtijbug.drools.platform.persistence.pojo.FactRuntimeType;
import org.chtijbug.drools.platform.persistence.pojo.RuleRuntime;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
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
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        UpdatedFactHistoryEvent updatedFactHistoryEvent = (UpdatedFactHistoryEvent) historyEvent;
        FactRuntime factRuntimeOldValue = new FactRuntime();
        factRuntimeOldValue.setFullClassName(updatedFactHistoryEvent.getObjectOldValue().getFullClassName());
        factRuntimeOldValue.setObjectVersion(updatedFactHistoryEvent.getObjectOldValue().getObjectVersion());
        factRuntimeOldValue.setJsonFact(updatedFactHistoryEvent.getObjectOldValue().getRealObject_JSON());
        factRuntimeOldValue.setModificationDate(updatedFactHistoryEvent.getDateEvent());
        factRuntimeOldValue.setFactRuntimeType(FactRuntimeType.UPDATED_OLDVALUE);
        FactRuntime factRuntimeNewValue = new FactRuntime();
        factRuntimeNewValue.setFullClassName(updatedFactHistoryEvent.getObjectNewValue().getFullClassName());
        factRuntimeNewValue.setObjectVersion(updatedFactHistoryEvent.getObjectNewValue().getObjectVersion());
        factRuntimeNewValue.setJsonFact(updatedFactHistoryEvent.getObjectNewValue().getRealObject_JSON());
        factRuntimeNewValue.setModificationDate(updatedFactHistoryEvent.getDateEvent());
        factRuntimeNewValue.setFactRuntimeType(FactRuntimeType.UPDATED_NEWVALUE);
        RuleRuntime existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInWorkflowGroupByRuleBaseIDAndSessionIDA(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
        if (existingInSessionRuleRuntime != null) {
            existingInSessionRuleRuntime.getThenFacts().add(factRuntimeOldValue);
            existingInSessionRuleRuntime.getThenFacts().add(factRuntimeNewValue);
            rulesRuntimeRepository.save(existingInSessionRuleRuntime);

        } else {
            existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInWorkflowGroupByRuleBaseIDAndSessionIDA(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
            if (existingInSessionRuleRuntime != null) {
                existingInSessionRuleRuntime.getThenFacts().add(factRuntimeOldValue);
                existingInSessionRuleRuntime.getThenFacts().add(factRuntimeNewValue);
                rulesRuntimeRepository.save(existingInSessionRuleRuntime);
            } else {
                SessionRuntime sessionRuntime = sessionRuntimeRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
                sessionRuntime.getFacts().add(factRuntimeOldValue);
                sessionRuntime.getFacts().add(factRuntimeNewValue);
                sessionRuntimeRepository.save(sessionRuntime);
            }
        }
        LOG.info("UpdatedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof UpdatedFactHistoryEvent;
    }
}
