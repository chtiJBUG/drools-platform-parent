package org.chtijbug.drools.platform.web.model;

import java.util.ArrayList;
import java.util.List;

public class RuleFlowGroupDetails {

    private String ruleflowGroup;
    private List<RuleExecutionDetails> allRuleExecutionDetails = new ArrayList<RuleExecutionDetails>();

    public RuleFlowGroupDetails() { /* nop */ }

    public RuleFlowGroupDetails(String ruleflowGroup, List<RuleExecutionDetails> allRuleExecutionDetails) {
        this.ruleflowGroup = ruleflowGroup;
        this.allRuleExecutionDetails = allRuleExecutionDetails;
    }

    public String getRuleflowGroup() {
        return ruleflowGroup;
    }

    public void setRuleflowGroup(String ruleflowGroup) {
        this.ruleflowGroup = ruleflowGroup;
    }

    public List<RuleExecutionDetails> getAllRuleExecutionDetails() {
        return allRuleExecutionDetails;
    }

    public void setAllRuleExecutionDetails(List<RuleExecutionDetails> allRuleExecutionDetails) {
        this.allRuleExecutionDetails = allRuleExecutionDetails;
    }

    public void addRuleExecution(RuleExecutionDetails ruleExecutionDetails) {
        this.allRuleExecutionDetails.add(ruleExecutionDetails);
    }
}
