package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class IndexRuleExecution {

    private Integer ruleBaseID;
    private Integer sessionId;
    private String ruleFlowGroup;
    private String ruleName;
    private RuleExecution ruleExecution;

    public IndexRuleExecution() {
    }

    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getRuleFlowGroup() {
        return ruleFlowGroup;
    }

    public void setRuleFlowGroup(String ruleFlowGroup) {
        this.ruleFlowGroup = ruleFlowGroup;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public RuleExecution getRuleExecution() {
        return ruleExecution;
    }


    public void setRuleExecution(RuleExecution ruleExecution) {
        this.ruleExecution = ruleExecution;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexRuleExecution{");
        sb.append("ruleBaseID=").append(ruleBaseID);
        sb.append(", sessionId=").append(sessionId);
        sb.append(", ruleFlowGroup='").append(ruleFlowGroup).append('\'');
        sb.append(", ruleName='").append(ruleName).append('\'');
        sb.append(", ruleExecution=").append(ruleExecution);
        sb.append('}');
        return sb.toString();
    }
}
