package org.chtijbug.drools.platform.entity.pojo;

import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;

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
@NamedQueries({
        @NamedQuery(name = "PlatformRuntime.findbyActivePlatformByRulebaseID",
                query = "SELECT platformRuntime " +
                        "FROM PlatformRuntime platformRuntime " +
                        "WHERE platformRuntime.ruleBaseID = :ruleBaseId " +
                        "AND platformRuntime.endDate is null"),
        @NamedQuery(name = "PlatformRuntime.findByRuleBaseIdAndStartDate",
                query = "SELECT platformRuntime " +
                        "FROM PlatformRuntime platformRuntime " +
                        "WHERE platformRuntime.ruleBaseID=:ruleBaseID " +
                        "AND platformRuntime.startDate=:startDate"),
        @NamedQuery(name = "PlatformRuntime.findActiveByHostName",
                        query = "SELECT platformRuntime " +
                                "FROM PlatformRuntime platformRuntime " +
                                "WHERE platformRuntime.hostname=:hostname " )
})
@Entity
//@Table(uniqueConstraints =
//@UniqueConstraint(columnNames = {"ruleBaseID", "startDate"}))
public class PlatformRuntime implements Serializable {

    @GeneratedValue
    @Id
    private long id;
    private String hostname;
    private int port;
    private String endPoint = "/runtime";
    @Column(nullable = false)
    private Date startDate;
    private Date endDate;
    private PlatformRuntimeStatus status;
    private int eventID;
    private int ruleBaseID;

    @OneToMany
    private List<DroolsRessource> droolsRessources = new ArrayList<DroolsRessource>();

    public PlatformRuntime() {
    }

    public PlatformRuntime(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
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

    public PlatformRuntimeStatus getStatus() {
        return status;
    }

    public void setStatus(PlatformRuntimeStatus status) {
        this.status = status;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(int ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public List<DroolsRessource> getDroolsRessources() {
        return droolsRessources;
    }

    public void setDroolsRessources(List<DroolsRessource> droolsRessources) {
        this.droolsRessources = droolsRessources;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformRuntime{");
        sb.append("id='").append(id).append('\'');
        sb.append(", hostname='").append(hostname).append('\'');
        sb.append(", port=").append(port);
        sb.append(", endPoint='").append(endPoint).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", status=").append(status);
        sb.append(", eventID=").append(eventID);
        sb.append(", ruleBaseID=").append(ruleBaseID);
        sb.append(", droolsRessources=").append(droolsRessources);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlatformRuntime that = (PlatformRuntime) o;

        if (ruleBaseID != that.ruleBaseID) return false;
        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + ruleBaseID;
        return result;
    }
}
