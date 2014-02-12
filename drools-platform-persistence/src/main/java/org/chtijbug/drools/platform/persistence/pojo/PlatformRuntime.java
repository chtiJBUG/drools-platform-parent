package org.chtijbug.drools.platform.persistence.pojo;

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
@Table(name = "platform_runtime")
public class PlatformRuntime implements Serializable {

<<<<<<< HEAD

    @Id
    @SequenceGenerator(name="platform_id_seq", sequenceName="drools_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_id_seq")
    private Long id;
=======
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
>>>>>>> 39fbc169b72e17bdaf7d7f92c61b01f89ab7c3a6
    @Column
    private String hostname;
    @Column
    private int port;
    @Column
    private String endPoint = "/runtime";
    @Column(nullable = false)
    private Date startDate;
    @Column
    private Date endDate;
    @Column
    private PlatformRuntimeStatus status;
    @Column
    private int eventID;
    @Column
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
