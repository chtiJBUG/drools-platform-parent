package org.chtijbug.drools.platform.persistence.pojo;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 14/02/14
 * Time: 09:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "session_execution", indexes = {@Index(columnList = "sessionId")})
@Cacheable(value = true)
public class SessionExecution {
    @Id
    @SequenceGenerator(name = "session_execution_id_seq", sequenceName = "session_execution_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_execution_id_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "platform_runtime_instance_id", referencedColumnName = "id")
    private PlatformRuntimeInstance platformRuntimeInstance;
    private Integer sessionId;
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private Integer startEventID;
    private Integer stopEventID;
    @Enumerated(EnumType.STRING)
    private SessionExecutionStatus sessionExecutionStatus;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sessionExecution")
    private List<RuleExecution> ruleExecutions = new ArrayList<RuleExecution>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sessionExecution")
    private List<ProcessExecution> processExecutions = new ArrayList<ProcessExecution>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Fact> facts = new ArrayList<Fact>();
    private Date processingStartDate;
    private Date processingStopDate;

    public SessionExecution() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlatformRuntimeInstance getPlatformRuntimeInstance() {
        return platformRuntimeInstance;
    }

    public void setPlatformRuntimeInstance(PlatformRuntimeInstance platformRuntimeInstance) {
        this.platformRuntimeInstance = platformRuntimeInstance;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
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

    public SessionExecutionStatus getSessionExecutionStatus() {
        return sessionExecutionStatus;
    }

    public void setSessionExecutionStatus(SessionExecutionStatus sessionExecutionStatus) {
        this.sessionExecutionStatus = sessionExecutionStatus;
    }

    public List<RuleExecution> getRuleExecutions() {
        return ruleExecutions;
    }

    public void setRuleExecutions(List<RuleExecution> ruleExecutions) {
        this.ruleExecutions = ruleExecutions;
    }

    public List<ProcessExecution> getProcessExecutions() {
        return processExecutions;
    }

    public void setProcessExecutions(List<ProcessExecution> processExecutions) {
        this.processExecutions = processExecutions;
    }

    public List<Fact> getFacts() {
        return facts;
    }

    public void setFacts(List<Fact> facts) {
        this.facts = facts;
    }

    public java.util.Collection<Fact> getFactsByType(final FactType factType) {
        if (this.getFacts().isEmpty())
            return Lists.newArrayList();
        return Collections2.filter(this.getFacts(), new Predicate<Fact>() {
            @Override
            public boolean apply(Fact fact) {
                return fact.getFactType().equals(factType);
            }
        });
    }

    public void setProcessingStartDate(Date processionStartDate) {
        this.processingStartDate = processionStartDate;
    }

    public Date getProcessingStartDate() {
        return processingStartDate;
    }

    public void setProcessingStopDate(Date processingStopDate) {
        this.processingStopDate = processingStopDate;
    }

    public Date getProcessingStopDate() {
        return processingStopDate;
    }
}
