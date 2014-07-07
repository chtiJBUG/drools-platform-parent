package org.chtijbug.drools.platform.persistence.pojo;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 21:15
 * To change this template use File | Settings | File Templates.
 */
public enum PlatformRuntimeInstanceStatus {
    INITMODE,STARTED,NOT_JOINGNABLE, STOPPED, CRASHED;

    public static PlatformRuntimeInstanceStatus getEnum(String assetStatus) {
        for (PlatformRuntimeInstanceStatus status : PlatformRuntimeInstanceStatus.values()) {
            if (status.name().equals(assetStatus))
                return status;
        }
        return null;
    }


}
