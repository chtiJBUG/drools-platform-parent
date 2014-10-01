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
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.UpdatedFactHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.Fact;
import org.chtijbug.drools.platform.persistence.pojo.FactType;
import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class UpdatedFactEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(UpdatedFactEventStrategy.class);
    @Autowired
    RuleExecutionRepositoryCacheService ruleExecutionRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        UpdatedFactHistoryEvent updatedFactHistoryEvent = (UpdatedFactHistoryEvent) historyEvent;
        Fact factOldValue = new Fact();
        factOldValue.setFullClassName(updatedFactHistoryEvent.getObjectOldValue().getFullClassName());
        factOldValue.setObjectVersion(updatedFactHistoryEvent.getObjectOldValue().getObjectVersion());
        factOldValue.setJsonFact(updatedFactHistoryEvent.getObjectOldValue().getRealObject_JSON());
        factOldValue.setModificationDate(updatedFactHistoryEvent.getDateEvent());
        factOldValue.setFactType(FactType.UPDATED_OLDVALUE);
        Fact factNewValue = new Fact();
        factNewValue.setFullClassName(updatedFactHistoryEvent.getObjectNewValue().getFullClassName());
        factNewValue.setObjectVersion(updatedFactHistoryEvent.getObjectNewValue().getObjectVersion());
        factNewValue.setJsonFact(updatedFactHistoryEvent.getObjectNewValue().getRealObject_JSON());
        factNewValue.setModificationDate(updatedFactHistoryEvent.getDateEvent());
        factNewValue.setFactType(FactType.UPDATED_NEWVALUE);
        RuleExecution existingInSessionRuleExecution = null;
        if (updatedFactHistoryEvent.getRuleName() == null) {  // updated from a session
            SessionExecution sessionExecution = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId());
            sessionExecution.getFacts().add(factOldValue);
            sessionExecution.getFacts().add(factNewValue);
            sessionExecutionRepository.save(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId(), sessionExecution);

        } else if (updatedFactHistoryEvent.getRuleName() != null && updatedFactHistoryEvent.getRuleflowGroup() == null) {   // updated from a rule that is not in a ruleflow/process
            existingInSessionRuleExecution = ruleExecutionRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId(), updatedFactHistoryEvent.getRuleName());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(factOldValue);
                existingInSessionRuleExecution.getThenFacts().add(factNewValue);
                ruleExecutionRepository.save(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId(), updatedFactHistoryEvent.getRuleflowGroup(), existingInSessionRuleExecution);
            }

        } else { // updated from a rule in a ruleflowgroup/process
            existingInSessionRuleExecution = ruleExecutionRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId(), updatedFactHistoryEvent.getRuleflowGroup(), updatedFactHistoryEvent.getRuleName());
            if (existingInSessionRuleExecution != null) {
                existingInSessionRuleExecution.getThenFacts().add(factOldValue);
                existingInSessionRuleExecution.getThenFacts().add(factNewValue);
                ruleExecutionRepository.save(updatedFactHistoryEvent.getRuleBaseID(), updatedFactHistoryEvent.getSessionId(), updatedFactHistoryEvent.getRuleflowGroup(), existingInSessionRuleExecution);
            }
        }


        LOG.debug("UpdatedFactHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof UpdatedFactHistoryEvent;
    }
}
