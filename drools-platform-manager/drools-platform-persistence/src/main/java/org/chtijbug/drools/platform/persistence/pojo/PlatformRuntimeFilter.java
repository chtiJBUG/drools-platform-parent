package org.chtijbug.drools.platform.persistence.pojo;

/**
 * Created by alexandre on 26/06/2014.
 */
public class PlatformRuntimeFilter {
    private String packageName;
    private String status;
    private String hostname;
    private String startDate;
    private String endDate;

    public PlatformRuntimeFilter() {/** nop */}

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
