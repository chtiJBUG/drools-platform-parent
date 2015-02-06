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
package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    private Long startEventID;

    private Long stopEventID;

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

    public Long getStartEventID() {
        return startEventID;
    }

    public void setStartEventID(Long startEventID) {
        this.startEventID = startEventID;
    }

    public Long getStopEventID() {
        return stopEventID;
    }

    public void setStopEventID(Long stopEventID) {
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
