package org.chtijbug.drools.platform.persistence.pojo;

import com.google.common.base.Objects;

public class PlatformRuntimeFilter {
    private String packageName;
    private String status;
    private String hostname;
    private String startDate;
    private String endDate;

    public PlatformRuntimeFilter() {/** nop */}

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

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("packageName", packageName)
                .add("status", status)
                .add("hostname", hostname)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .toString();
    }
}
