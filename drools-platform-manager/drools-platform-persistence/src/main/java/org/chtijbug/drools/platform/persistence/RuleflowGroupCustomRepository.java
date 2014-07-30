package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 29/07/14
 * Time: 19:04
 * To change this template use File | Settings | File Templates.
 */
public interface RuleflowGroupCustomRepository {


    List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("processInstanceID") String processInstanceID, @Param("ruleFlowGroupName") String ruleFlowGroupName);

    RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("processInstanceID") String processInstanceID, @Param("ruleFlowGroupName") String ruleFlowGroupName);

    List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("ruleFlowGroupName") String ruleFlowGroupName);

    RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("ruleFlowGroupName") String ruleFlowGroupName);


}
