package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsNodeType;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterNodeInstanceTriggeredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessExecutionRepository;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupRuntimeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class AfterNodeInstanceTriggeredEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterNodeInstanceTriggeredEventStrategy.class);
    @Autowired
    private RuleflowGroupRuntimeRepository ruleflowGroupRuntimeRepository;

    @Autowired
    private ProcessExecutionRepository processExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterNodeInstanceTriggeredHistoryEvent afterNodeInstanceTriggeredHistoryEvent = (AfterNodeInstanceTriggeredHistoryEvent) historyEvent;
        if (afterNodeInstanceTriggeredHistoryEvent.getNodeInstance().getNode().getNodeType() == DroolsNodeType.RuleNode) {
            List<RuleflowGroupRuntime> ruleflowGroupRuntimes = ruleflowGroupRuntimeRepository.findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(afterNodeInstanceTriggeredHistoryEvent.getRuleBaseID(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), afterNodeInstanceTriggeredHistoryEvent.getProcessInstance().getId(), afterNodeInstanceTriggeredHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
            for (RuleflowGroupRuntime ruleflowGroupRuntime : ruleflowGroupRuntimes) {
                ruleflowGroupRuntime.setEndDate(new Date());
                ruleflowGroupRuntime.setRuleflowGroupRuntimeStatus(RuleflowGroupRuntimeStatus.CRASHED);
                ruleflowGroupRuntimeRepository.save(ruleflowGroupRuntime);
            }

            ProcessExecution processExecution = processExecutionRepository.findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(afterNodeInstanceTriggeredHistoryEvent.getRuleBaseID(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), afterNodeInstanceTriggeredHistoryEvent.getProcessInstance().getId());

            RuleflowGroupRuntime ruleflowGroupRuntime = new RuleflowGroupRuntime();
            ruleflowGroupRuntime.setProcessExecution(processExecution);
            ruleflowGroupRuntime.setStartDate(afterNodeInstanceTriggeredHistoryEvent.getDateEvent());
            ruleflowGroupRuntime.setRuleflowGroupRuntimeStatus(RuleflowGroupRuntimeStatus.STARTED);
            ruleflowGroupRuntime.setRuleflowGroup(afterNodeInstanceTriggeredHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
            ruleflowGroupRuntimeRepository.save(ruleflowGroupRuntime);
        }
        LOG.debug("AfterNodeInstanceTriggeredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeInstanceTriggeredHistoryEvent;
    }
}
