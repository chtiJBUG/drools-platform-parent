package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstanceStatus;

public class RuntimeStatusObject {
    private PlatformRuntimeInstanceStatus runtimeStatus;
    private String code;
    private String description;

    public RuntimeStatusObject(){ /* nop */ }

    public RuntimeStatusObject(PlatformRuntimeInstanceStatus runtimeStatus, String description) {
        this.runtimeStatus = runtimeStatus;
        this.code = runtimeStatus.name();
        this.description = description;
    }

    public PlatformRuntimeInstanceStatus getRuntimeStatus() { return runtimeStatus; }

    public void setRuntimeStatus(PlatformRuntimeInstanceStatus runtimeStatus) { this.runtimeStatus = runtimeStatus; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }
}
