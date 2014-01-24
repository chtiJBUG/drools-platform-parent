package org.chtijbug.drools.platform.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:20
 * To change this template use File | Settings | File Templates.
 */
public class PlatformRuntime implements Serializable{
    public static String class_name="platformruntime";
    public static String var_hostname="hostname";
    public static String var_port="port";
    public static String var_endpoint="endpoint";
    public static String var_startdate="startdate";
    public static String var_enddate="enddate";
    public static String var_status="status";
    public static String var_eventid="eventid";
    public static String var_rulebaseid="rulebaseid";
    public static String var_droolsressources="droolsressources";

    private String orientdbId;
    private String hostname;
    private int port;
    private String endPoint ="/runtime";
    private Date startDate;
    private Date endDate;
    private PlatformRuntimeStatus status;
    private int eventID;
    private int ruleBaseID  ;
    private ArrayList<DroolsRessource> droolsRessources = new ArrayList<DroolsRessource>();

    public PlatformRuntime() {
    }

    public PlatformRuntime(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getOrientdbId() {
        return orientdbId;
    }

    public void setOrientdbId(String orientdbId) {
        this.orientdbId = orientdbId;
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

    public ArrayList<DroolsRessource> getDroolsRessources() {
        return droolsRessources;
    }

    public void setDroolsRessources(ArrayList<DroolsRessource> droolsRessources) {
        this.droolsRessources = droolsRessources;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformRuntime{");
        sb.append("orientdbId='").append(orientdbId).append('\'');
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
}
