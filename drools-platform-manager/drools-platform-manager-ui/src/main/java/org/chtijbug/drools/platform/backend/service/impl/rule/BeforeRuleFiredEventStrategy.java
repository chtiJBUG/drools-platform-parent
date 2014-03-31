package org.chtijbug.drools.platform.backend.service.impl.rule;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.BeforeRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRuntimeRepository;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.FactRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RuleRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {


        BeforeRuleFiredHistoryEvent beforeRuleFiredHistoryEvent = (BeforeRuleFiredHistoryEvent) historyEvent;

        RuleflowGroupRuntime ruleflowGroupRuntime = ruleflowGroupRuntimeRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(beforeRuleFiredHistoryEvent.getRuleBaseID(), beforeRuleFiredHistoryEvent.getSessionId(), beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup());
        RuleRuntime ruleRuntime = new RuleRuntime();
        ruleRuntime.setRuleflowGroupRuntime(ruleflowGroupRuntime);
        ruleRuntime.setStartDate(beforeRuleFiredHistoryEvent.getDateEvent());
        ruleRuntime.setRuleName(beforeRuleFiredHistoryEvent.getRule().getRuleName());
        ruleRuntime.setPackageName(beforeRuleFiredHistoryEvent.getRule().getRulePackageName()) ;
        for (DroolsFactObject droolsFactObject : beforeRuleFiredHistoryEvent.getWhenObjects()){
            FactRuntime factRuntime = new FactRuntime();
            factRuntime.setJsonFact(droolsFactObject.getRealObject_JSON());
            factRuntime.setObjectVersion(droolsFactObject.getObjectVersion());
            factRuntime.setFullClassName(droolsFactObject.getFullClassName());
            ruleRuntime.getWhenFacts().add(factRuntime);
        }
        rulesRuntimeRepository.save(ruleRuntime);
        LOG.info("BeforeRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeRuleFiredHistoryEvent;
    }
}
