package org.chtijbug.drools.platform.backend.service.impl.rule;

import javassist.ClassPool;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRuntimeRepository;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.RuleRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupRuntime;
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
public class AfterRuleFiredEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterRuleFiredEventStrategy.class);

    @Autowired
    private RulesRuntimeRepository rulesRuntimeRepository;




    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterRuleFiredHistoryEvent afterRuleFiredHistoryEvent = (AfterRuleFiredHistoryEvent) historyEvent;

        RuleRuntime ruleRuntime=null;



        ruleRuntime = rulesRuntimeRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(afterRuleFiredHistoryEvent.getRuleBaseID(),afterRuleFiredHistoryEvent.getSessionId(),afterRuleFiredHistoryEvent.getRule().getRuleFlowGroup(),afterRuleFiredHistoryEvent.getRule().getRuleName());
        if (ruleRuntime != null) {
            ruleRuntime.setEndDate(afterRuleFiredHistoryEvent.getDateEvent());
            rulesRuntimeRepository.save(ruleRuntime);
        } else {
            ruleRuntime = rulesRuntimeRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(afterRuleFiredHistoryEvent.getRuleBaseID(),afterRuleFiredHistoryEvent.getSessionId(),afterRuleFiredHistoryEvent.getRule().getRuleName());
            ruleRuntime.setEndDate(afterRuleFiredHistoryEvent.getDateEvent());
            rulesRuntimeRepository.save(ruleRuntime);
        }
        LOG.debug("AfterRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterRuleFiredHistoryEvent;
    }
}
