/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.rule;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        if (platformRuntimeMode==PlatformRuntimeMode.Debug) {
            return true;
        }
        else{
            return false;
        }
    }
}
