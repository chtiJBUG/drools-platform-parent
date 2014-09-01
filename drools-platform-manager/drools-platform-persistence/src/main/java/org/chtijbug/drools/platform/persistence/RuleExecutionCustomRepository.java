package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;

/**
 * Created by IntelliJ IDEA.
 * Date: 30/07/14
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public interface RuleExecutionCustomRepository {

    RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleFlowGroup, String ruleName);


    RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleName);

}
