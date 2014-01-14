package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.entity.PlatformRuntime;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:23
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeStorageManager {

    public List<PlatformRuntime> findRunningPlatformRuntime(String hostname);

    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID);

    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID, String hostname);

    public void save(PlatformRuntime platformRuntime);
}
