package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:23
 * To change this template use File | Settings | File Templates.
 */
public interface RuntimeStorageManager {

    public List<PlatformRuntime> findRunningPlatformRuntime(String hostname);

    public PlatformRuntime findRunningPlatformRuntime(int ruleBaseID);

    public PlatformRuntime findRunningPlatformRuntime(int ruleBaseID, Date startDate);

    public void deletePlatformRuntime(PlatformRuntime platformRuntime);

    public void save(PlatformRuntime platformRuntime);

    public void update(PlatformRuntime platformRuntime);

    public PlatformRuntime findPlatformRuntime(long id);




}
