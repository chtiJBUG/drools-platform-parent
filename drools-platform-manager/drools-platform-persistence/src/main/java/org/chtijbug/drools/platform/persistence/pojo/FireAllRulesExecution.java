package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 26/03/14
 * Time: 09:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "fireallrules_execution")
public class FireAllRulesExecution {

    @Id
    @SequenceGenerator(name = "fireallrules_execution_id_seq", sequenceName = "fireallrules_execution_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fireallrules_execution_id_seq")
    private Long id;
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;

    private Long nbreRulesFired;
    private Long maxNbreRulesDefinedForSession;

    private Long executionTime;

    @ManyToOne
    private SessionExecution sessionExecution;

    private Integer eventID;

    @Enumerated(EnumType.STRING)
    private FireAllRulesExecutionStatus fireAllRulesExecutionStatus;

    public FireAllRulesExecution() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public SessionExecution getSessionExecution() {
        return sessionExecution;
    }

    public void setSessionExecution(SessionExecution sessionExecution) {
        this.sessionExecution = sessionExecution;
    }

    public Long getNbreRulesFired() {
        return nbreRulesFired;
    }

    public void setNbreRulesFired(Long nbreRulesFired) {
        this.nbreRulesFired = nbreRulesFired;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public FireAllRulesExecutionStatus getFireAllRulesExecutionStatus() {
        return fireAllRulesExecutionStatus;
    }

    public void setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus fireAllRulesExecutionStatus) {
        this.fireAllRulesExecutionStatus = fireAllRulesExecutionStatus;
    }

    public Long getMaxNbreRulesDefinedForSession() {
        return maxNbreRulesDefinedForSession;
    }

    public void setMaxNbreRulesDefinedForSession(Long maxNbreRulesDefinedForSession) {
        this.maxNbreRulesDefinedForSession = maxNbreRulesDefinedForSession;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FireRulesRuntime{");
        sb.append("id=").append(id);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", nbreRulesFired=").append(nbreRulesFired);
        sb.append(", maxNbreRulesDefinedForSession=").append(maxNbreRulesDefinedForSession);
        sb.append(", executionTime=").append(executionTime);
        sb.append(", eventID=").append(eventID);
        sb.append(", fireRulesRuntimeStatus=").append(fireAllRulesExecutionStatus);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FireAllRulesExecution)) return false;

        FireAllRulesExecution that = (FireAllRulesExecution) o;

        if (!sessionExecution.equals(that.sessionExecution)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + sessionExecution.hashCode();
        return result;
    }

}
