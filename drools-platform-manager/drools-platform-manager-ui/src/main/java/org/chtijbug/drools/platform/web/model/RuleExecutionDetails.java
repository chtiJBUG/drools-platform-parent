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

import org.chtijbug.drools.platform.persistence.pojo.Fact;

import java.util.List;

public class RuleExecutionDetails {

    private String ruleName;
    private String packageName;
    List<Fact> whenFacts;
    List<Fact> thenFacts;
    private RuleAssetDetails ruleAsset;

    public RuleExecutionDetails() { /* nop */ }

    public RuleExecutionDetails(String ruleName, String packageName, List<Fact> whenFacts, List<Fact> thenFacts, RuleAssetDetails ruleAsset) {
        this.ruleName = ruleName;
        this.packageName = packageName;
        this.whenFacts = whenFacts;
        this.thenFacts = thenFacts;
        this.ruleAsset = ruleAsset;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<Fact> getWhenFacts() {
        return whenFacts;
    }

    public void setWhenFacts(List<Fact> whenFacts) {
        this.whenFacts = whenFacts;
    }

    public List<Fact> getThenFacts() {
        return thenFacts;
    }

    public void setThenFacts(List<Fact> thenFacts) {
        this.thenFacts = thenFacts;
    }

    public RuleAssetDetails getRuleAsset() {
        return ruleAsset;
    }

    public void setRuleAsset(RuleAssetDetails ruleAsset) {
        this.ruleAsset = ruleAsset;
    }
}
