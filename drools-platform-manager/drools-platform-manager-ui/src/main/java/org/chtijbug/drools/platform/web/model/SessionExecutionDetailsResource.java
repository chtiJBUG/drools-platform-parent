/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.web.model;

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

    public SessionExecutionDetailsResource duplicate() {
        SessionExecutionDetailsResource result = new SessionExecutionDetailsResource();
        result.setInputObject(this.inputObject);
        result.setOutputObject(this.outputObject);
        if (this.executionStats != null) {
            result.setExecutionStats(this.executionStats.duplicate());
        }
        result.setProcessDetails(this.processDetails.duplicate());
        for (RuleFlowGroupDetails detail : allRuleFlowGroupDetails) {
            RuleFlowGroupDetails newRFG = new RuleFlowGroupDetails();
            result.addRuleFlowGroup(newRFG);
            newRFG.setRuleflowGroup(detail.getRuleflowGroup());
            newRFG.setPosition(detail.getPosition());
            newRFG.setNbRuleToDisplay(detail.getNbRuleToDisplay());
            for (int j = detail.getPosition(); j < Math.min(detail.getPosition() + detail.getNbRuleToDisplay(), detail.getAllRuleExecutionDetails().size()); j++) {
                RuleExecutionDetails rule = detail.getAllRuleExecutionDetails().get(j);
                newRFG.getAllRuleExecutionDetails().add(rule);
            }
        }
        return result;
    }
}
