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
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupStatus;
import org.springframework.stereotype.Component;


@Component
public class AfterNodeLeftEventStrategy extends AbstractMemoryEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterNodeLeftEventStrategy.class);


    @Override
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {
        AfterNodeLeftHistoryEvent afterNodeLeftHistoryEvent = (AfterNodeLeftHistoryEvent) historyEvent;
        if (afterNodeLeftHistoryEvent.getNodeInstance().getNode().getNodeType() == DroolsNodeType.RuleNode) {
            String ruleFlowName = afterNodeLeftHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName();
            RuleflowGroup ruleflowGroup = sessionContext.findRuleFlowGroup(ruleFlowName);
            ruleflowGroup.setEndDate(afterNodeLeftHistoryEvent.getDateEvent());
            ruleflowGroup.setStopEventID(afterNodeLeftHistoryEvent.getEventID());
            ruleflowGroup.setRuleflowGroupStatus(RuleflowGroupStatus.STOPPED);
        }
        // afterNodeLeftHistoryEvent.get
        LOG.debug("AfterNodeLeftHistoryEvent " + historyEvent.toString());
    }
    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeLeftHistoryEvent;
    }

}
