package org.chtijbug.drools.platform.web.model;

/**
 * Created by alexandre on 12/06/2014.
 */
public class RuntimeFilter {
    private String packageName;
    private String status;
    private String hostname;
    private String startDate;
    private String endDate;

    public RuntimeFilter(String packageName, String status, String hostname, String startDate, String endDate) {
        this.packageName = packageName;
        this.status = status;
        this.hostname = hostname;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getpackageName() {
        return packageName;
    }

    public void setpackageName(String packageName) {
        this.packageName = packageName;
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
