package org.chtijbug.drools.platform.persistence.pojo;

import org.chtijbug.drools.platform.entity.FireRulesRuntimeStatus;
import org.chtijbug.drools.platform.entity.ProcessRuntimeStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 26/03/14
 * Time: 09:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "firerules_runtime")
public class FireRulesRuntime {

    @Id
    @SequenceGenerator(name = "firerules_id_seq", sequenceName = "firerules_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "firerules_id_seq")
    private Long id;
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;

    private Long nbreRulesFired;
    private Long maxNbreRulesDefinedForSession;

    private Long executionTime;

    @ManyToOne
    private SessionRuntime sessionRuntime;

    private Integer eventID;

    @Enumerated(EnumType.STRING)
    private FireRulesRuntimeStatus fireRulesRuntimeStatus;

    public FireRulesRuntime() {
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

    public SessionRuntime getSessionRuntime() {
        return sessionRuntime;
    }

    public void setSessionRuntime(SessionRuntime sessionRuntime) {
        this.sessionRuntime = sessionRuntime;
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

    public FireRulesRuntimeStatus getFireRulesRuntimeStatus() {
        return fireRulesRuntimeStatus;
    }

    public void setFireRulesRuntimeStatus(FireRulesRuntimeStatus fireRulesRuntimeStatus) {
        this.fireRulesRuntimeStatus = fireRulesRuntimeStatus;
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
        sb.append(", fireRulesRuntimeStatus=").append(fireRulesRuntimeStatus);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FireRulesRuntime)) return false;

        FireRulesRuntime that = (FireRulesRuntime) o;

        if (!sessionRuntime.equals(that.sessionRuntime)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + sessionRuntime.hashCode();
        return result;
    }

}
