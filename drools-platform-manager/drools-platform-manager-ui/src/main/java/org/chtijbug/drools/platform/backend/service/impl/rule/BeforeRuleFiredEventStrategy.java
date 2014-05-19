package org.chtijbug.drools.platform.backend.service.impl.rule;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.BeforeRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRuntimeRepository;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.*;
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
public class BeforeRuleFiredEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeRuleFiredEventStrategy.class);
    @Autowired
    private RuleflowGroupRuntimeRepository ruleflowGroupRuntimeRepository;

    @Autowired
    private RulesRuntimeRepository rulesRuntimeRepository;

    @Autowired
    private SessionExecutionRepository sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        BeforeRuleFiredHistoryEvent beforeRuleFiredHistoryEvent = (BeforeRuleFiredHistoryEvent) historyEvent;
        RuleRuntime ruleRuntime = new RuleRuntime();
        if (beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup() != null && beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup().length() > 0) {
            RuleflowGroupRuntime ruleflowGroupRuntime = ruleflowGroupRuntimeRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(beforeRuleFiredHistoryEvent.getRuleBaseID(), beforeRuleFiredHistoryEvent.getSessionId(), beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup());
            ruleRuntime.setRuleflowGroupRuntime(ruleflowGroupRuntime);
        } else {
            SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(beforeRuleFiredHistoryEvent.getRuleBaseID(), beforeRuleFiredHistoryEvent.getSessionId());
            ruleRuntime.setSessionExecution(sessionExecution);
        }
        ruleRuntime.setStartDate(beforeRuleFiredHistoryEvent.getDateEvent());
        ruleRuntime.setRuleName(beforeRuleFiredHistoryEvent.getRule().getRuleName());
        ruleRuntime.setPackageName(beforeRuleFiredHistoryEvent.getRule().getRulePackageName());
        for (DroolsFactObject droolsFactObject : beforeRuleFiredHistoryEvent.getWhenObjects()) {
            FactRuntime factRuntime = new FactRuntime();

            factRuntime.setJsonFact(droolsFactObject.getRealObject_JSON());
            factRuntime.setFactRuntimeType(FactRuntimeType.WHEN);
            factRuntime.setObjectVersion(droolsFactObject.getObjectVersion());
            factRuntime.setFullClassName(droolsFactObject.getFullClassName());
            ruleRuntime.getWhenFacts().add(factRuntime);
        }
        rulesRuntimeRepository.save(ruleRuntime);
        LOG.debug("BeforeRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeRuleFiredHistoryEvent;
    }
}
