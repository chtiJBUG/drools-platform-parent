package org.chtijbug.drools.platform.backend.service.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.InsertedFactHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.FactRuntime;
import org.chtijbug.drools.platform.persistence.pojo.FactRuntimeType;
import org.chtijbug.drools.platform.persistence.pojo.RuleRuntime;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    RulesRuntimeRepository rulesRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        InsertedFactHistoryEvent insertedFactHistoryEvent = (InsertedFactHistoryEvent)historyEvent;
        FactRuntime factRuntime = new FactRuntime();
        factRuntime.setFullClassName(insertedFactHistoryEvent.getInsertedObject().getFullClassName());
        factRuntime.setObjectVersion(insertedFactHistoryEvent.getInsertedObject().getObjectVersion());
        factRuntime.setJsonFact(insertedFactHistoryEvent.getInsertedObject().getRealObject_JSON());
        factRuntime.setModificationDate(insertedFactHistoryEvent.getDateEvent());
        factRuntime.setFactRuntimeType(FactRuntimeType.INSERTED);
        RuleRuntime existingInSessionRuleRuntime= rulesRuntimeRepository.findActiveRuleInWorkflowGroupByRuleBaseIDAndSessionIDA(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
        if (existingInSessionRuleRuntime!= null) {
            existingInSessionRuleRuntime.getThenFacts().add(factRuntime) ;
            rulesRuntimeRepository.save(existingInSessionRuleRuntime);

        } else {
            existingInSessionRuleRuntime = rulesRuntimeRepository.findActiveRuleInWorkflowGroupByRuleBaseIDAndSessionIDA(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
            if (existingInSessionRuleRuntime!= null){
                existingInSessionRuleRuntime.getThenFacts().add(factRuntime) ;
                rulesRuntimeRepository.save(existingInSessionRuleRuntime);
            } else{
                SessionRuntime sessionRuntime = sessionRuntimeRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(insertedFactHistoryEvent.getRuleBaseID(), insertedFactHistoryEvent.getSessionId());
                sessionRuntime.getFacts().add(factRuntime);
                sessionRuntimeRepository.save(sessionRuntime);
            }
        }
        LOG.info("InsertedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof InsertedFactHistoryEvent;
    }
}
