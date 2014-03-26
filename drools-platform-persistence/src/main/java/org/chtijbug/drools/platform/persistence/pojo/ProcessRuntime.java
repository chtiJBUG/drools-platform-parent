package org.chtijbug.drools.platform.persistence.pojo;

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
@Table(name = "process_runtime")
public class ProcessRuntime {

    @Id
    @SequenceGenerator(name = "process_id_seq", sequenceName = "process_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_id_seq")
    private Long id;

    @Column(nullable = false)
    private String processName;

    @Column(nullable = false)
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private ProcessRuntimeStatus processRuntimeStatus;

    private Integer eventID;

    @ManyToOne
    private SessionRuntime sessionRuntime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
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

    public ProcessRuntimeStatus getProcessRuntimeStatus() {
        return processRuntimeStatus;
    }

    public void setProcessRuntimeStatus(ProcessRuntimeStatus processRuntimeStatus) {
        this.processRuntimeStatus = processRuntimeStatus;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProcessRuntime{");
        sb.append("id=").append(id);
        sb.append(", processName='").append(processName).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", processRuntimeStatus=").append(processRuntimeStatus);
        sb.append(", eventID=").append(eventID);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessRuntime)) return false;

        ProcessRuntime that = (ProcessRuntime) o;

        if (!processName.equals(that.processName)) return false;
        if (!sessionRuntime.equals(that.sessionRuntime)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = processName.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + sessionRuntime.hashCode();
        return result;
    }

}
