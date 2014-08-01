package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.rule;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleExecutionRepositoryCacheService;
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
    private RuleExecutionRepositoryCacheService ruleExecutionRepository;


    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterRuleFiredHistoryEvent afterRuleFiredHistoryEvent = (AfterRuleFiredHistoryEvent) historyEvent;

        RuleExecution ruleExecution = null;
        if (afterRuleFiredHistoryEvent.getRule().getRuleFlowGroup() != null) {
            ruleExecution = ruleExecutionRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(afterRuleFiredHistoryEvent.getRuleBaseID(), afterRuleFiredHistoryEvent.getSessionId(), afterRuleFiredHistoryEvent.getRule().getRuleFlowGroup(), afterRuleFiredHistoryEvent.getRule().getRuleName());
            ruleExecution.setEndDate(afterRuleFiredHistoryEvent.getDateEvent());
            ruleExecution.setStopEventID(afterRuleFiredHistoryEvent.getEventID());
            ruleExecutionRepository.save(afterRuleFiredHistoryEvent.getRuleBaseID(), afterRuleFiredHistoryEvent.getSessionId(), afterRuleFiredHistoryEvent.getRule().getRuleFlowGroup(), ruleExecution);
        } else {
            ruleExecution = ruleExecutionRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(afterRuleFiredHistoryEvent.getRuleBaseID(), afterRuleFiredHistoryEvent.getSessionId(), afterRuleFiredHistoryEvent.getRule().getRuleName());
            ruleExecution.setEndDate(afterRuleFiredHistoryEvent.getDateEvent());
            ruleExecution.setStopEventID(afterRuleFiredHistoryEvent.getEventID());
            ruleExecutionRepository.save(afterRuleFiredHistoryEvent.getRuleBaseID(), afterRuleFiredHistoryEvent.getSessionId(), afterRuleFiredHistoryEvent.getRule().getRuleFlowGroup(), ruleExecution);
        }
        LOG.debug("AfterRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterRuleFiredHistoryEvent;
    }
}
