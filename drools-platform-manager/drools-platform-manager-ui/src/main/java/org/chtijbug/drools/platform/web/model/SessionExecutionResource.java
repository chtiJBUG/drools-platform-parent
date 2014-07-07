package org.chtijbug.drools.platform.web.model;

public class SessionExecutionResource {
    private Integer ruleBaseID;
    private String rulePackage;
    private String version;
    private Integer sessionId;
    private String runtimeURL;
    private String guvnorUrl;
    private String hostname;
    private String status;
    private String StartDate;
    private String EndDate;


    public SessionExecutionResource() { /*nop*/ }

    public SessionExecutionResource(Integer ruleBaseID, String rulePackage, String version, Integer sessionId, String runtimeURL, String guvnorUrl, String hostname, String status, String startDate, String endDate) {
        this.ruleBaseID = ruleBaseID;
        this.rulePackage = rulePackage;
        this.version = version;
        this.sessionId = sessionId;
        this.runtimeURL = runtimeURL;
        this.guvnorUrl = guvnorUrl;
        this.hostname = hostname;
        this.status = status;
        StartDate = startDate;
        EndDate = endDate;
    }

    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public String getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(String rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getRuntimeURL() {
        return runtimeURL;
    }

    public void setRuntimeURL(String runtimeURL) {
        this.runtimeURL = runtimeURL;
    }

    public String getGuvnorUrl() {
        return guvnorUrl;
    }

    public void setGuvnorUrl(String guvnorUrl) {
        this.guvnorUrl = guvnorUrl;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }
}
