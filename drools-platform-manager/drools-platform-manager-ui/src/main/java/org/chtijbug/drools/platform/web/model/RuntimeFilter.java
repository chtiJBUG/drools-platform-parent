package org.chtijbug.drools.platform.web.model;

/**
 * Created by alexandre on 12/06/2014.
 */
public class RuntimeFilter {
    private String rulePackage;
    private String status;
    private String hostname;
    private String startDate;
    private String endDate;

    public RuntimeFilter(String rulePackage, String status, String hostname, String startDate, String endDate) {
        this.rulePackage = rulePackage;
        this.status = status;
        this.hostname = hostname;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(String rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
