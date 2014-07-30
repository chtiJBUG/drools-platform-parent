package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 29/07/14
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
public interface RuleflowGroupCustomRepository {


    List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName);

    RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName);

    List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName);

    RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName);


}
