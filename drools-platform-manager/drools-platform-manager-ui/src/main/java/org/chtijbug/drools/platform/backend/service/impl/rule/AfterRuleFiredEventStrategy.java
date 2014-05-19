package org.chtijbug.drools.platform.backend.service.impl.rule;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RulesRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
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

        RuleExecution ruleExecution =null;



        ruleExecution = rulesRuntimeRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(afterRuleFiredHistoryEvent.getRuleBaseID(),afterRuleFiredHistoryEvent.getSessionId(),afterRuleFiredHistoryEvent.getRule().getRuleFlowGroup(),afterRuleFiredHistoryEvent.getRule().getRuleName());
        if (ruleExecution != null) {
            ruleExecution.setEndDate(afterRuleFiredHistoryEvent.getDateEvent());
            rulesRuntimeRepository.save(ruleExecution);
        } else {
            ruleExecution = rulesRuntimeRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(afterRuleFiredHistoryEvent.getRuleBaseID(),afterRuleFiredHistoryEvent.getSessionId(),afterRuleFiredHistoryEvent.getRule().getRuleName());
            ruleExecution.setEndDate(afterRuleFiredHistoryEvent.getDateEvent());
            rulesRuntimeRepository.save(ruleExecution);
        }
        LOG.debug("AfterRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterRuleFiredHistoryEvent;
    }
}
