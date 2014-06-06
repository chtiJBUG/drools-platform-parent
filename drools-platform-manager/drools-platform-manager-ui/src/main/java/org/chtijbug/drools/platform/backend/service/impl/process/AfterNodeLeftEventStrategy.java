package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsNodeType;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterNodeLeftHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.RuleflowGroupRepository;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroupStatus;
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
public class AfterNodeLeftEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterNodeLeftEventStrategy.class);

    @Autowired
    RuleflowGroupRepository ruleflowGroupRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterNodeLeftHistoryEvent afterNodeLeftHistoryEvent = (AfterNodeLeftHistoryEvent) historyEvent;
        if (afterNodeLeftHistoryEvent.getNodeInstance().getNode().getNodeType() == DroolsNodeType.RuleNode) {

            RuleflowGroup ruleflowGroup = ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(afterNodeLeftHistoryEvent.getRuleBaseID(), afterNodeLeftHistoryEvent.getSessionId(), afterNodeLeftHistoryEvent.getProcessInstance().getId(), afterNodeLeftHistoryEvent.getNodeInstance().getNode().getRuleflowGroupName());
            ruleflowGroup.setEndDate(afterNodeLeftHistoryEvent.getDateEvent());
            ruleflowGroup.setStopEventID(afterNodeLeftHistoryEvent.getEventID());
            ruleflowGroup.setRuleflowGroupStatus(RuleflowGroupStatus.STOPPED);
            ruleflowGroupRepository.save(ruleflowGroup);
        }
        // afterNodeLeftHistoryEvent.get
        LOG.debug("AfterNodeLeftHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeLeftHistoryEvent;
    }
}
