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
import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.BeforeRuleFiredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.springframework.stereotype.Component;


@Component
public class BeforeRuleFiredEventStrategy extends AbstractMemoryEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeRuleFiredEventStrategy.class);




    @Override
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {
        BeforeRuleFiredHistoryEvent beforeRuleFiredHistoryEvent = (BeforeRuleFiredHistoryEvent) historyEvent;
        RuleExecution ruleExecution = new RuleExecution();
        sessionContext.setRuleExecution(ruleExecution);
        if (beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup() != null && beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup().length() > 0) {
            String ruleFlowName = beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup();
            RuleflowGroup ruleflowGroup = sessionContext.findRuleFlowGroup(ruleFlowName);
            if (ruleflowGroup == null) {
                ruleflowGroup = new RuleflowGroup();
                ruleflowGroup.setRuleflowGroup(beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup());
                sessionContext.getRuleflowGroups().add(ruleflowGroup);
                if (sessionContext.getProcessExecution()!=null) {
                    sessionContext.getProcessExecution().getRuleflowGroups().add(ruleflowGroup);
                }
            }

            ruleflowGroup.getRuleExecutionList().add(ruleExecution);
        } else {
            SessionExecution sessionExecution = sessionContext.getSessionExecution();
            sessionExecution.getRuleExecutions().add(ruleExecution);
        }
        ruleExecution.setStartDate(beforeRuleFiredHistoryEvent.getDateEvent());
        ruleExecution.setRuleName(beforeRuleFiredHistoryEvent.getRule().getRuleName());
        ruleExecution.setPackageName(beforeRuleFiredHistoryEvent.getRule().getRulePackageName());
        RuleAsset ruleAsset = new RuleAsset(ruleExecution.getPackageName(), ruleExecution.getRuleName());
        ruleExecution.setRuleAsset(ruleAsset);
        ruleExecution.setStartEventID(beforeRuleFiredHistoryEvent.getEventID());
        for (DroolsFactObject droolsFactObject : beforeRuleFiredHistoryEvent.getWhenObjects()) {
            if (droolsFactObject != null) {
                Fact fact = new Fact();
                fact.setJsonFact(droolsFactObject.getRealObject_JSON());
                fact.setFactType(FactType.WHEN);
                fact.setObjectVersion(droolsFactObject.getObjectVersion());
                fact.setFullClassName(droolsFactObject.getFullClassName());
                fact.setEventid(beforeRuleFiredHistoryEvent.getEventID());
                ruleExecution.getWhenFacts().add(fact);
            }
        }
        LOG.debug("BeforeRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeRuleFiredHistoryEvent;
    }


}
