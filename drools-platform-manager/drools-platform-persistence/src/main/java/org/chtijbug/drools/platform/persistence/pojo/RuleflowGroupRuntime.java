package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 28/03/14
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ruleflowgroup_runtime")
public class RuleflowGroupRuntime {

    @Id
    @SequenceGenerator(name = "ruleflowgroup_id_seq", sequenceName = "ruleflowgroup_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ruleflowgroup_id_seq")
    private Long id;

    @Column(nullable = false)
    private String ruleflowGroup;

    @ManyToOne
    @JoinColumn(name = "process_runtime_id_fk")
    private ProcessExecution processExecution;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ruleflowgroup_runtime_id_fk")
    private List<RuleExecution> ruleExecutionList = new ArrayList<RuleExecution>();

    @Column(nullable = false)
    private Date startDate;

    private Date endDate;

    @Enumerated(EnumType.STRING)
    private RuleflowGroupRuntimeStatus ruleflowGroupRuntimeStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleflowGroup() {
        return ruleflowGroup;
    }

    public void setRuleflowGroup(String ruleflowGroup) {
        this.ruleflowGroup = ruleflowGroup;
    }

    public ProcessExecution getProcessExecution() {
        return processExecution;
    }

    public void setProcessExecution(ProcessExecution processExecution) {
        this.processExecution = processExecution;
    }

    public List<RuleExecution> getRuleExecutionList() {
        return ruleExecutionList;
    }

    public void setRuleExecutionList(List<RuleExecution> ruleExecutionList) {
        this.ruleExecutionList = ruleExecutionList;
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

    public RuleflowGroupRuntimeStatus getRuleflowGroupRuntimeStatus() {
        return ruleflowGroupRuntimeStatus;
    }

    public void setRuleflowGroupRuntimeStatus(RuleflowGroupRuntimeStatus ruleflowGroupRuntimeStatus) {
        this.ruleflowGroupRuntimeStatus = ruleflowGroupRuntimeStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuleflowGroupRuntime)) return false;

        RuleflowGroupRuntime that = (RuleflowGroupRuntime) o;

        if (!processExecution.equals(that.processExecution)) return false;
        if (!ruleflowGroup.equals(that.ruleflowGroup)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ruleflowGroup.hashCode();
        result = 31 * result + processExecution.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RuleflowGroupRuntime{");
        sb.append("id=").append(id);
        sb.append(", ruleflowGroup='").append(ruleflowGroup).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", ruleflowGroupRuntimeStatus=").append(ruleflowGroupRuntimeStatus);
        sb.append('}');
        return sb.toString();
    }
}
