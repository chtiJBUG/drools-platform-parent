package org.chtijbug.drools.platform.backend.service.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.DeletedFactHistoryEvent;
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
public class DeleteFactEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(DeleteFactEventStrategy.class);

    @Autowired
    RulesRuntimeRepository rulesRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        DeletedFactHistoryEvent deletedFactHistoryEvent = (DeletedFactHistoryEvent) historyEvent;
        FactRuntime factRuntime = new FactRuntime();
        factRuntime.setFullClassName(deletedFactHistoryEvent.getDeletedObject().getFullClassName());
        factRuntime.setObjectVersion(deletedFactHistoryEvent.getDeletedObject().getObjectVersion());
        factRuntime.setJsonFact(deletedFactHistoryEvent.getDeletedObject().getRealObject_JSON());
        factRuntime.setModificationDate(deletedFactHistoryEvent.getDateEvent());
        factRuntime.setFactRuntimeType(FactRuntimeType.INSERTED);
        RuleRuntime existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInSessionByRuleBaseIDAndSessionID(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId());
        if (existingInSessionRuleRuntime != null) {
            existingInSessionRuleRuntime.getThenFacts().add(factRuntime);
            rulesRuntimeRepository.save(existingInSessionRuleRuntime);

        } else {
            existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId());
            if (existingInSessionRuleRuntime != null) {
                existingInSessionRuleRuntime.getThenFacts().add(factRuntime);
                rulesRuntimeRepository.save(existingInSessionRuleRuntime);
            } else {
                SessionRuntime sessionRuntime = sessionRuntimeRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(deletedFactHistoryEvent.getRuleBaseID(), deletedFactHistoryEvent.getSessionId());
                sessionRuntime.getFacts().add(factRuntime);
                sessionRuntimeRepository.save(sessionRuntime);
            }
        }
        LOG.debug("DeletedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof DeletedFactHistoryEvent;
    }
}
