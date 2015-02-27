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
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsNodeType;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterNodeLeftHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class AfterNodeLeftEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterNodeLeftEventStrategy.class);

    @Autowired
    RuleflowGroupRepositoryCacheService ruleflowGroupRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterNodeLeftHistoryEvent afterNodeLeftHistoryEvent = (AfterNodeLeftHistoryEvent) historyEvent;
        if (afterNodeLeftHistoryEvent.getNodeInstance().getNode().getNodeType() == DroolsNodeType.RuleNode) {

            RuleflowGroup ruleflowGroup = ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(afterNodeLeftHistoryEvent.getRuleBaseID(), afterNodeLeftHistoryEvent.getSessionId(), afterNodeLeftHistoryEvent.getProcessInstance().getId(), afterNodeLeftHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
            ruleflowGroup.setEndDate(afterNodeLeftHistoryEvent.getDateEvent());
            ruleflowGroup.setStopEventID(afterNodeLeftHistoryEvent.getEventID());
            ruleflowGroup.setRuleflowGroupStatus(RuleflowGroupStatus.STOPPED);
            ruleflowGroupRepository.save(afterNodeLeftHistoryEvent.getRuleBaseID(), afterNodeLeftHistoryEvent.getSessionId(), afterNodeLeftHistoryEvent.getSessionId(), ruleflowGroup);
        }
        // afterNodeLeftHistoryEvent.get
        LOG.debug("AfterNodeLeftHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeLeftHistoryEvent;
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
