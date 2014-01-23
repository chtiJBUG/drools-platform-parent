package org.chtijbug.drools.platform.rules.management;

import java.util.ArrayList;
import java.util.List;

public enum AssetStatus {
    DEV,
    INT,
    PROD,
    NONE;

    public static AssetStatus getEnum(String assetStatus) {
        for(AssetStatus status : AssetStatus.values()) {
            if (status.name().equals(assetStatus))
                return status;
        }
        return null;
    }

    public AssetStatus getNextStatus() {
        AssetStatus result;
        switch (this) {
            case DEV:
                result = INT;
                break;
            case INT:
                result = PROD;
                break;
            default:
                result = null;
        }
        return result;
    }
    public List<AssetStatus> getPreviousStatus() {
        List<AssetStatus> result = new ArrayList<AssetStatus>();
            switch (this) {
                case INT:
                    result.add(DEV);
                    break;
                case PROD:
                    result.add(INT);
                    break;
            }
            return result;
        }

}
