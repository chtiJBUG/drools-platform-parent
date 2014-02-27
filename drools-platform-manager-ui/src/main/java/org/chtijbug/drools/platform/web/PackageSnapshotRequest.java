package org.chtijbug.drools.platform.web;

import com.google.common.collect.Lists;
import org.chtijbug.drools.platform.rules.management.AssetStatus;

import java.util.List;

public class PackageSnapshotRequest {
    public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
    private List<AssetStatus> assetStatuses = Lists.newArrayList();
    private String version;
    private Boolean isRelease = false;

    public List<AssetStatus> getAssetStatuses() {
        return assetStatuses;
    }

    public void setAssetStatuses(List<AssetStatus> assetStatuses) {
        this.assetStatuses = assetStatuses;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getIsRelease() {
        return isRelease;
    }

    public void setIsRelease(Boolean isRelease) {
        this.isRelease = isRelease;
    }

    public String constructVersionName() {
        if (isRelease)
            return version;
        return version.concat(SNAPSHOT_SUFFIX);
    }
}
