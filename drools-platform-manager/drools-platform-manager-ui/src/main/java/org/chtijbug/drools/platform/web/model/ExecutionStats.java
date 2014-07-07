package org.chtijbug.drools.platform.web.model;

/**
 * Created by alexandre on 03/07/2014.
 */
public class ExecutionStats {

    private String duration;
    private Integer firedRulesCount;
    private String status;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getFiredRulesCount() {
        return firedRulesCount;
    }

    public void setFiredRulesCount(Integer firedRulesCount) {
        this.firedRulesCount = firedRulesCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

