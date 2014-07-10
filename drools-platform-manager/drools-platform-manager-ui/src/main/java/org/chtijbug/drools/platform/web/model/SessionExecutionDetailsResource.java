package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.Fact;

import java.util.ArrayList;
import java.util.List;

public class SessionExecutionDetailsResource {

    ProcessDetails processDetails;
    ExecutionStats executionStats;
    List<RuleFlowGroupDetails> allRuleFlowGroupDetails = new ArrayList<>();
    private String inputObject;
    private String outputObject;

    public SessionExecutionDetailsResource() { /* nop */ }

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

    public String getInputObject() {
        return inputObject;
    }

    public void setInputObject(String inputObject) {
        this.inputObject = inputObject;
    }

    public String getOutputObject() {
        return outputObject;
    }

    public void setOutputObject(String outputObject) {
        this.outputObject = outputObject;
    }
}
