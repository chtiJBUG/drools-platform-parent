package org.chtijbug.drools.platform.rules.management;

import java.util.ArrayList;
import java.util.List;

public enum AssetStatus {
    DEV,
    INT,
    PROD,
    NONE;

    public static AssetStatus getEnum(String assetStatus) {
        for (AssetStatus status : AssetStatus.values()) {
            if (status.name().equals(assetStatus))
                return status;
        }
        return null;
    }

    public AssetStatus getNextStatus() {
        AssetStatus result = null;
        switch (this) {
            case DEV:
                result = INT;
                break;
            case INT:
                result = PROD;
                break;
        }
        return result;
    }

    public AssetStatus getPreviousStatus() {
        AssetStatus result = null;
        switch (this) {
            case INT:
                result = DEV;
                break;
            case PROD:
                result = INT;
                break;
        }
        return result;
    }

}
