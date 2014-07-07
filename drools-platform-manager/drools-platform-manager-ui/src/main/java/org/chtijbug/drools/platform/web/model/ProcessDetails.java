package org.chtijbug.drools.platform.web.model;

/**
 * Created by alexandre on 03/07/2014.
 */
public class ProcessDetails {
    private String processName;
    private String processExecutionStatus;
    private String processVersion;
    private String processType;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessExecutionStatus() {
        return processExecutionStatus;
    }

    public void setProcessExecutionStatus(String processExecutionStatus) {
        this.processExecutionStatus = processExecutionStatus;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
}
