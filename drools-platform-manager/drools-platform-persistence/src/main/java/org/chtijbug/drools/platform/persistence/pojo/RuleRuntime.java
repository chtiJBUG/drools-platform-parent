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
    @SequenceGenerator(name = "rules_id_seq", sequenceName = "rules_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rules_id_seq")
    private Long id;

    private String ruleName;
    private String packageName;
    private Date startDate;
    private Date endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "rule_whenFacts")
    private List<FactRuntime> whenFacts = new ArrayList<FactRuntime>() ;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "rule_thenFacts")
    private List<FactRuntime> thenFacts = new ArrayList<FactRuntime>() ;

    @ManyToOne
    @JoinColumn(name="ruleflowgroup_runtime_id_fk")
    private RuleflowGroupRuntime ruleflowGroupRuntime;

    @ManyToOne
    @JoinColumn(name="session_runtime_id_fk")
    private SessionExecution sessionExecution;

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

    public void setSessionExecution(SessionExecution sessionExecution) {
        this.sessionExecution = sessionExecution;
    }

    public SessionExecution getSessionExecution() {
        return sessionExecution;
    }

    public List<FactRuntime> getThenFacts() {
        return thenFacts;
    }

    public void setThenFacts(List<FactRuntime> thenFacts) {
        this.thenFacts = thenFacts;
    }
}
