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
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleAssetRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.RuleExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class BeforeRuleFiredEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeRuleFiredEventStrategy.class);
    @Autowired
    private RuleflowGroupRepositoryCacheService ruleflowGroupRepository;

    @Autowired
    private RuleExecutionRepositoryCacheService ruleExecutionRepository;

    @Autowired
    private SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Autowired
    private RuleAssetRepositoryCacheService ruleAssetRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        BeforeRuleFiredHistoryEvent beforeRuleFiredHistoryEvent = (BeforeRuleFiredHistoryEvent) historyEvent;
        RuleExecution ruleExecution = new RuleExecution();
        if (beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup() != null && beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup().length() > 0) {
            RuleflowGroup ruleflowGroup = ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(beforeRuleFiredHistoryEvent.getRuleBaseID(), beforeRuleFiredHistoryEvent.getSessionId(), beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup());
            ruleExecution.setRuleflowGroup(ruleflowGroup);
        } else {
            SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(beforeRuleFiredHistoryEvent.getRuleBaseID(), beforeRuleFiredHistoryEvent.getSessionId());
            ruleExecution.setSessionExecution(sessionExecution);
        }
        ruleExecution.setStartDate(beforeRuleFiredHistoryEvent.getDateEvent());
        ruleExecution.setRuleName(beforeRuleFiredHistoryEvent.getRule().getRuleName());
        ruleExecution.setPackageName(beforeRuleFiredHistoryEvent.getRule().getRulePackageName());
        RuleAsset ruleAsset = ruleAssetRepository.findByPackageNameAndAssetName(ruleExecution.getPackageName(), ruleExecution.getRuleName());
        if (ruleAsset != null) {
            ruleExecution.setRuleAsset(ruleAsset);
        } else {
            ruleAsset = new RuleAsset(ruleExecution.getPackageName(), ruleExecution.getRuleName());
            ruleAssetRepository.save(ruleAsset);
            ruleExecution.setRuleAsset(ruleAsset);
        }

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
        ruleExecutionRepository.save(beforeRuleFiredHistoryEvent.getRuleBaseID(), beforeRuleFiredHistoryEvent.getSessionId(), beforeRuleFiredHistoryEvent.getRule().getRuleFlowGroup(), ruleExecution);
        LOG.debug("BeforeRuleFiredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeRuleFiredHistoryEvent;
    }
}
