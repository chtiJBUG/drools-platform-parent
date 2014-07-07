package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.Fact;

import java.util.List;

public class RuleExecutionDetails {

    private String ruleName;
    private String packageName;
    List<Fact> whenFacts;
    List<Fact> thenFacts;

    public RuleExecutionDetails() {
        /* nop */
    }

    public RuleExecutionDetails(String ruleName, String packageName, List<Fact> whenFacts, List<Fact> thenFacts) {
        this.ruleName = ruleName;
        this.packageName = packageName;
        this.whenFacts = whenFacts;
        this.thenFacts = thenFacts;
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
}
