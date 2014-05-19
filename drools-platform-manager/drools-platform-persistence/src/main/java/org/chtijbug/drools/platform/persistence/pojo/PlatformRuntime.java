package org.chtijbug.drools.platform.persistence.pojo;

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
@Table(name = "platform_runtime")
public class PlatformRuntime implements Serializable {

    @Id
    @SequenceGenerator(name = "platform_id_seq", sequenceName = "drools_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_id_seq")
    private Long id;
    private String hostname;
    private Integer port;
    private String endPoint = "/runtime";
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private Date shutdowDate;
    @Enumerated(EnumType.STRING)
    private PlatformRuntimeStatus status;
    private Integer eventID;
    private Integer ruleBaseID;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_runtime_id_fk")
    private List<DroolsResource> droolsRessources = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "platform_runtime_id_fk")
    private List<SessionRuntime> sessionRuntimes = new ArrayList<SessionRuntime>();
    @ManyToOne
    @JoinColumn(name = "platform_instance_id_fk")
    private PlatformRuntimeDefinition platformRuntimeDefinition;


    public PlatformRuntime() {
    }

    public PlatformRuntime(String hostname, int port) {
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

    public PlatformRuntimeStatus getStatus() {
        return status;
    }

    public void setStatus(PlatformRuntimeStatus status) {
        this.status = status;
    }

    public Integer getEventID() {
        return eventID;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
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

    public List<SessionRuntime> getSessionRuntimes() {
        return sessionRuntimes;
    }

    public void setSessionRuntimes(List<SessionRuntime> sessionRuntimes) {
        this.sessionRuntimes = sessionRuntimes;
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
        if (!(o instanceof PlatformRuntime)) return false;

        PlatformRuntime that = (PlatformRuntime) o;

        if (!eventID.equals(that.eventID)) return false;
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
        result = 31 * result + eventID.hashCode();
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
                append("eventID", eventID).
                append("ruleBaseID", ruleBaseID).

                toString();
    }
}
