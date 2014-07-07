package org.chtijbug.drools.platform.web.model;

import java.util.ArrayList;
import java.util.List;

public class SessionExecutionDetailsResource {

    ProcessDetails processDetails;
    ExecutionStats executionStats;
    List<RuleFlowGroupDetails> allRuleFlowGroupDetails = new ArrayList<>();

    public SessionExecutionDetailsResource() { /* nop */ }

    public SessionExecutionDetailsResource(ProcessDetails processDetails, ExecutionStats executionStats, List<RuleFlowGroupDetails> allRuleFlowGroupDetails) {
        this.processDetails = processDetails;
        this.executionStats = executionStats;
        this.allRuleFlowGroupDetails = allRuleFlowGroupDetails;
    }

    public ProcessDetails getProcessDetails() {
        return processDetails;
    }

    public void setProcessDetails(ProcessDetails processDetails) {
        this.processDetails = processDetails;
    }

    public ExecutionStats getExecutionStats() {
        return executionStats;
    }

    public void setExecutionStats(ExecutionStats executionStats) {
        this.executionStats = executionStats;
    }

    public List<RuleFlowGroupDetails> getAllRuleFlowGroupDetails() {
        return allRuleFlowGroupDetails;
    }

    public void setAllRuleFlowGroupDetails(List<RuleFlowGroupDetails> allRuleFlowGroupDetails) {
        this.allRuleFlowGroupDetails = allRuleFlowGroupDetails;
    }

    public void addRuleFlowGroup(RuleFlowGroupDetails ruleFlowGroupDetails) {
        this.allRuleFlowGroupDetails.add(ruleFlowGroupDetails);
    }


}
