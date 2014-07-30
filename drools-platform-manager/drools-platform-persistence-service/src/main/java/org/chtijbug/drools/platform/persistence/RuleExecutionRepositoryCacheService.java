package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class RuleExecutionRepositoryCacheService {
    @Autowired
    RuleExecutionRepository ruleExecutionRepository;

    public RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleFlowGroup, String ruleName) {
        return this.ruleExecutionRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(ruleBaseID, sessionId, ruleFlowGroup, ruleName);
    }


    public RuleExecution findActiveRuleInSessionByRuleBaseIDAndSessionID(Integer ruleBaseID, Integer sessionId) {
        return this.ruleExecutionRepository.findActiveRuleInSessionByRuleBaseIDAndSessionID(ruleBaseID, sessionId);
    }


    public RuleExecution findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(Integer ruleBaseID, Integer sessionId) {
        return this.ruleExecutionRepository.findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(ruleBaseID, sessionId);
    }


    public RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleName) {
        return this.ruleExecutionRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(ruleBaseID, sessionId, ruleName);
    }


    public void save(RuleExecution ruleExecution) {
        this.ruleExecutionRepository.save(ruleExecution);
    }
}
