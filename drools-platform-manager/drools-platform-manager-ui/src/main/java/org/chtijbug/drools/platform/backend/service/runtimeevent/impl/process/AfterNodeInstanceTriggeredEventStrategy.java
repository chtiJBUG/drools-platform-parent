package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsNodeType;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterNodeInstanceTriggeredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupStatus;
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
    private RuleflowGroupRepositoryCacheService ruleflowGroupRepository;

    @Autowired
    private ProcessExecutionRepositoryCacheService processExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterNodeInstanceTriggeredHistoryEvent afterNodeInstanceTriggeredHistoryEvent = (AfterNodeInstanceTriggeredHistoryEvent) historyEvent;
        if (afterNodeInstanceTriggeredHistoryEvent.getNodeInstance().getNode().getNodeType() == DroolsNodeType.RuleNode) {
            List<RuleflowGroup> ruleflowGroups = ruleflowGroupRepository.findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(afterNodeInstanceTriggeredHistoryEvent.getRuleBaseID(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), afterNodeInstanceTriggeredHistoryEvent.getProcessInstance().getId(), afterNodeInstanceTriggeredHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
            for (RuleflowGroup ruleflowGroup : ruleflowGroups) {
                ruleflowGroup.setEndDate(new Date());
                ruleflowGroup.setRuleflowGroupStatus(RuleflowGroupStatus.CRASHED);
                ruleflowGroupRepository.save(afterNodeInstanceTriggeredHistoryEvent.getRuleBaseID(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), ruleflowGroup);
            }

            ProcessExecution processExecution = processExecutionRepository.findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(afterNodeInstanceTriggeredHistoryEvent.getRuleBaseID(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), afterNodeInstanceTriggeredHistoryEvent.getProcessInstance().getId());

            RuleflowGroup ruleflowGroup = new RuleflowGroup();
            ruleflowGroup.setProcessExecution(processExecution);
            ruleflowGroup.setStartDate(afterNodeInstanceTriggeredHistoryEvent.getDateEvent());
            ruleflowGroup.setStartEventID(afterNodeInstanceTriggeredHistoryEvent.getEventID());
            ruleflowGroup.setRuleflowGroupStatus(RuleflowGroupStatus.STARTED);
            ruleflowGroup.setRuleflowGroup(afterNodeInstanceTriggeredHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
            ruleflowGroupRepository.save(afterNodeInstanceTriggeredHistoryEvent.getRuleBaseID(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), afterNodeInstanceTriggeredHistoryEvent.getSessionId(), ruleflowGroup);
        }
        LOG.debug("AfterNodeInstanceTriggeredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeInstanceTriggeredHistoryEvent;
    }
}
