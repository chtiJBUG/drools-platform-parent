package org.chtijbug.drools.platform.persistence.pojo;

import com.google.common.base.Objects;

import java.util.Date;

public class PlatformRuntimeFilter {
    private String packageName;
    private String status;
    private String hostname;
    private Date startDate;
    private Date endDate;
    private String onlyRunningInstances;

    private Page page;

    public PlatformRuntimeFilter() {/* nop */}

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
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

    public String getOnlyRunningInstances() {
        return onlyRunningInstances;
    }

    public void setOnlyRunningInstances(String onlyRunningInstances) {
        this.onlyRunningInstances = onlyRunningInstances;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("packageName", packageName)
                .add("status", status)
                .add("hostname", hostname)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("onlyRunningInstances", onlyRunningInstances)
                .toString();
    }
}
