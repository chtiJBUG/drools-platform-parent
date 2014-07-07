package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 26/03/14
 * Time: 09:38
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "process_execution")
public class ProcessExecution {

    @Id
    @SequenceGenerator(name = "process_execution_id_seq", sequenceName = "process_execution_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "process_execution_id_seq")
    private Long id;

    @Column(nullable = false)
    private String processName;

    @Column(nullable = false)
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private ProcessExecutionStatus processExecutionStatus;

    private Integer startEventID;

    private Integer stopEventID;

    @ManyToOne
    @JoinColumn(name = "sessionexecution_id", referencedColumnName = "id")
    private SessionExecution sessionExecution;

    private String ProcessInstanceId;

    private String processPackageName;

    private String processVersion;

    private String processType;

    private String processId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "process_execution_id_fk")
    private List<RuleflowGroup> ruleflowGroups = new ArrayList<RuleflowGroup>();


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

    public SessionExecution getSessionExecution() {
        return sessionExecution;
    }

    public void setSessionExecution(SessionExecution sessionExecution) {
        this.sessionExecution = sessionExecution;
    }

    public ProcessExecutionStatus getProcessExecutionStatus() {
        return processExecutionStatus;
    }

    public void setProcessExecutionStatus(ProcessExecutionStatus processExecutionStatus) {
        this.processExecutionStatus = processExecutionStatus;
    }

    public String getProcessInstanceId() {
        return ProcessInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        ProcessInstanceId = processInstanceId;
    }

    public String getProcessPackageName() {
        return processPackageName;
    }

    public void setProcessPackageName(String processPackageName) {
        this.processPackageName = processPackageName;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<RuleflowGroup> getRuleflowGroups() {
        return ruleflowGroups;
    }

    public void setRuleflowGroups(List<RuleflowGroup> ruleflowGroups) {
        this.ruleflowGroups = ruleflowGroups;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProcessRuntime{");
        sb.append("id=").append(id);
        sb.append(", processName='").append(processName).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", processRuntimeStatus=").append(processExecutionStatus);
        sb.append(", startEventID=").append(startEventID);
        sb.append(", ProcessInstanceId='").append(ProcessInstanceId).append('\'');
        sb.append(", processPackageName='").append(processPackageName).append('\'');
        sb.append(", processVersion='").append(processVersion).append('\'');
        sb.append(", processType='").append(processType).append('\'');
        sb.append(", processId='").append(processId).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessExecution)) return false;

        ProcessExecution that = (ProcessExecution) o;

        if (!processName.equals(that.processName)) return false;
        if (!sessionExecution.equals(that.sessionExecution)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = processName.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + sessionExecution.hashCode();
        return result;
    }

}
