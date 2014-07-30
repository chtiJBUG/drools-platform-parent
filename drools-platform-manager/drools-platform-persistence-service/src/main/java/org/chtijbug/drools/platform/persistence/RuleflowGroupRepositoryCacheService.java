package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class RuleflowGroupRepositoryCacheService {
    @Autowired
    RuleflowGroupRepository ruleflowGroupRepository;

    public List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName) {
        return this.ruleflowGroupRepository.findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(ruleBaseID, sessionID, processInstanceID, ruleFlowGroupName);
    }

    public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName) {
        return this.ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(ruleBaseID, sessionID, processInstanceID, ruleFlowGroupName);

    }

    public List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName) {
        return this.ruleflowGroupRepository.findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName(ruleBaseID, sessionID, ruleFlowGroupName);

    }

    public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName) {
        return this.ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(ruleBaseID, sessionID, ruleFlowGroupName);

    }

    public void save(RuleflowGroup ruleflowGroup) {
        this.ruleflowGroupRepository.save(ruleflowGroup);
    }
}
