package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.rules.management.AssetStatus;

public class AssetStatusObject {
    private AssetStatus assetStatus;
    private String code;
    private String description;

    public AssetStatusObject() { /* nop */}

    public AssetStatusObject(AssetStatus assetStatus, String description) {
        this.assetStatus = assetStatus;
        this.code = assetStatus.name();
        this.description = description;
    }

    public AssetStatus getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
