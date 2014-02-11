package org.chtijbug.drools.platform.persistence.impl;


import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.entity.pojo.DroolsRessource;
import org.chtijbug.drools.platform.entity.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.RuntimeStorageManager;
import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class RuntimeStorageManagerImpl implements RuntimeStorageManager {

    @Autowired
    IPlatformRuntimeDao platformRuntimeDao;

    @Override
    public List<PlatformRuntime> findRunningPlatformRuntime(String hostname) {
    return null;
    }

    @Override
    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID) {
        return null;
    }

    @Override
    public List<PlatformRuntime> findRunningPlatformRuntime(int ruleBaseID, String hostname) {
      return null;
    }

    @Override
    public void deletePlatformRuntime(String orientdbID) {

    }
    @Override
    public PlatformRuntime getPlatformRuntime(String orientdbID) {
        return null;
    }


    @Override
    public void updatePlatformRuntime(String orientdbID, PlatformRuntime platformRuntime) {

    }

    @Override
    public void save(PlatformRuntime platformRuntime) {


    }

    @Override
    public void save(String platformRuntimedbID, DroolsRessource droolsRessource) {

    
        }

}
