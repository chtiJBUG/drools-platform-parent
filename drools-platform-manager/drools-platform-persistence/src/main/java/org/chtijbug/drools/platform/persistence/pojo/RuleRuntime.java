package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 28/03/14
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "rule_runtime")
public class RuleRuntime {

    @Id
    @SequenceGenerator(name = "rule_id_seq", sequenceName = "rule_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_id_seq")
    private Long id;

    private String ruleName;
    private String packageName;
    private Date startDate;
    private Date endDate;

    @OneToMany
    private List<FactRuntime> whenFacts = new ArrayList<FactRuntime>() ;



    @ManyToOne
    private RuleflowGroupRuntime ruleflowGroupRuntime;

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

    public RuleflowGroupRuntime getRuleflowGroupRuntime() {
        return ruleflowGroupRuntime;
    }

    public void setRuleflowGroupRuntime(RuleflowGroupRuntime ruleflowGroupRuntime) {
        this.ruleflowGroupRuntime = ruleflowGroupRuntime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FactRuntime> getWhenFacts() {
        return whenFacts;
    }

    public void setWhenFacts(List<FactRuntime> whenFacts) {
        this.whenFacts = whenFacts;
    }
}
