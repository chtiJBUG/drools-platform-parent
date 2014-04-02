package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsNodeType;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterNodeLeftHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupRuntimeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class AfterNodeLeftEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterNodeLeftEventStrategy.class);

    @Autowired
    RuleflowGroupRuntimeRepository ruleflowGroupRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterNodeLeftHistoryEvent afterNodeLeftHistoryEvent = (AfterNodeLeftHistoryEvent) historyEvent;
        if (afterNodeLeftHistoryEvent.getNodeInstance().getNode().getNodeType() == DroolsNodeType.RuleNode) {

            RuleflowGroupRuntime ruleflowGroupRuntime = ruleflowGroupRuntimeRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(afterNodeLeftHistoryEvent.getRuleBaseID(), afterNodeLeftHistoryEvent.getSessionId(), afterNodeLeftHistoryEvent.getProcessInstance().getId(), afterNodeLeftHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
             ruleflowGroupRuntime.setEndDate(afterNodeLeftHistoryEvent.getDateEvent());
            ruleflowGroupRuntime.setRuleflowGroupRuntimeStatus(RuleflowGroupRuntimeStatus.STOPPED);
            ruleflowGroupRuntimeRepository.save(ruleflowGroupRuntime);
        }
        // afterNodeLeftHistoryEvent.get
        LOG.info("AfterNodeLeftHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeLeftHistoryEvent;
    }
}
