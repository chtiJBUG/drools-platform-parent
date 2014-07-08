package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.Fact;

import java.util.ArrayList;
import java.util.List;

public class SessionExecutionDetailsResource {

    ProcessDetails processDetails;
    ExecutionStats executionStats;
    List<RuleFlowGroupDetails> allRuleFlowGroupDetails = new ArrayList<>();
    private Fact inputObject;
    private Fact outputObject;

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

    public Fact getInputObject() {
        return inputObject;
    }

    public void setInputObject(Fact inputObject) {
        this.inputObject = inputObject;
    }

    public Fact getOutputObject() {
        return outputObject;
    }

    public void setOutputObject(Fact outputObject) {
        this.outputObject = outputObject;
    }
}
