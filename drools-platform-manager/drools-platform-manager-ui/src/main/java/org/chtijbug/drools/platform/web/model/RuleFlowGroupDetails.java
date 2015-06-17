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

public class RuleFlowGroupDetails {

    private String ruleflowGroup;
    private List<RuleExecutionDetails> allRuleExecutionDetails = new ArrayList<RuleExecutionDetails>();

    private int nbRuleToDisplay = 20;

    private int position = 0;


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

    public int getNbRuleToDisplay() {
        return nbRuleToDisplay;
    }

    public void setNbRuleToDisplay(int nbRuleToDisplay) {
        this.nbRuleToDisplay = nbRuleToDisplay;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
