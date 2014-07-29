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
@Table(name = "rule_execution", indexes = {@Index(columnList = "ruleName"), @Index(columnList = "packageName")})
public class RuleExecution {

    @Id
    @SequenceGenerator(name = "rule_execution_id_seq", sequenceName = "rule_execution_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_execution_id_seq")
    private Long id;

    private String ruleName;
    private String packageName;
    private Date startDate;
    private Date endDate;

    private Integer startEventID;

    private Integer stopEventID;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "rule_execution_whenFacts")
    private List<Fact> whenFacts = new ArrayList<Fact>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "rule_execution_thenFacts")
    private List<Fact> thenFacts = new ArrayList<Fact>();

    @ManyToOne
    @JoinColumn(name = "ruleflowgroup_execution_id_fk", referencedColumnName = "id")
    private RuleflowGroup ruleflowGroup;

    @ManyToOne
    @JoinColumn(name = "session_execution_id_fk", referencedColumnName = "id")
    private SessionExecution sessionExecution;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_asset_id_fk", referencedColumnName = "id")
    private RuleAsset ruleAsset;

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

    public RuleflowGroup getRuleflowGroup() {
        return ruleflowGroup;
    }

    public void setRuleflowGroup(RuleflowGroup ruleflowGroup) {
        this.ruleflowGroup = ruleflowGroup;
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

    public Integer getStartEventID() {
        return startEventID;
    }

    public void setStartEventID(Integer startEventID) {
        this.startEventID = startEventID;
    }

    public Integer getStopEventID() {
        return stopEventID;
    }

    public void setStopEventID(Integer stopEventID) {
        this.stopEventID = stopEventID;
    }

    public List<Fact> getWhenFacts() {
        return whenFacts;
    }

    public void setWhenFacts(List<Fact> whenFacts) {
        this.whenFacts = whenFacts;
    }

    public void setSessionExecution(SessionExecution sessionExecution) {
        this.sessionExecution = sessionExecution;
    }

    public SessionExecution getSessionExecution() {
        return sessionExecution;
    }

    public List<Fact> getThenFacts() {
        return thenFacts;
    }

    public void setThenFacts(List<Fact> thenFacts) {
        this.thenFacts = thenFacts;
    }

    public RuleAsset getRuleAsset() {
        return ruleAsset;
    }

    public void setRuleAsset(RuleAsset ruleAsset) {
        this.ruleAsset = ruleAsset;
    }
}
