package org.chtijbug.drools.platform.persistence.pojo;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:20
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "platform_runtime_instance")
public class PlatformRuntimeInstance implements Serializable {

    @Id
    @SequenceGenerator(name = "platform_runtime_instance_id_seq", sequenceName = "platform_runtime_instance_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_runtime_instance_id_seq")
    private Long id;
    private String hostname;
    private Integer port;
    private String endPoint = "/runtime";
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private Date shutdowDate;
    @Enumerated(EnumType.STRING)
    private PlatformRuntimeInstanceStatus status;
    private Integer startEventID;
    private Integer stopEventID;
    private Integer ruleBaseID;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_runtime_instance_id_fk")
    private List<DroolsResource> droolsRessources = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_runtime_instance_id_fk")
    private List<SessionExecution> sessionExecutions = new ArrayList<SessionExecution>();
    @ManyToOne
    @JoinColumn(name = "platform_runtime_instance_id_fk")
    private PlatformRuntimeDefinition platformRuntimeDefinition;


    public PlatformRuntimeInstance() {
    }

    public PlatformRuntimeInstance(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
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

    public Date getShutdowDate() {
        return shutdowDate;
    }

    public void setShutdowDate(Date shutdowDate) {
        this.shutdowDate = shutdowDate;
    }

    public PlatformRuntimeInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(PlatformRuntimeInstanceStatus status) {
        this.status = status;
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

    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public List<DroolsResource> getDroolsRessources() {
        return droolsRessources;
    }

    public void setDroolsRessources(List<DroolsResource> droolsResources) {
        this.droolsRessources = droolsResources;
    }

    public List<SessionExecution> getSessionExecutions() {
        return sessionExecutions;
    }

    public void setSessionExecutions(List<SessionExecution> sessionExecutions) {
        this.sessionExecutions = sessionExecutions;
    }

    public PlatformRuntimeDefinition getPlatformRuntimeDefinition() {
        return platformRuntimeDefinition;
    }

    public void setPlatformRuntimeDefinition(PlatformRuntimeDefinition platformRuntimeDefinition) {
        this.platformRuntimeDefinition = platformRuntimeDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlatformRuntimeInstance)) return false;

        PlatformRuntimeInstance that = (PlatformRuntimeInstance) o;

        if (!startEventID.equals(that.startEventID)) return false;
        if (!hostname.equals(that.hostname)) return false;
        if (!port.equals(that.port)) return false;
        if (!ruleBaseID.equals(that.ruleBaseID)) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hostname.hashCode();
        result = 31 * result + port.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + startEventID.hashCode();
        result = 31 * result + ruleBaseID.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("id", id).
                append("hostname", hostname).
                append("port", port).
                append("endPoint", endPoint).
                append("startDate", startDate).
                append("endDate", endDate).
                append("status", status).
                append("startEventID", startEventID).
                append("ruleBaseID", ruleBaseID).

                toString();
    }
}
