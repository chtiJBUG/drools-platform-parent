package org.chtijbug.drools.platform.persistence.impl;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.RuntimeStorageManager;
import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
     return this.platformRuntimeDao.findActiveByHostName(hostname);
    }

    @Override
    public PlatformRuntime findRunningPlatformRuntime(int ruleBaseID) {
        return this.platformRuntimeDao.findbyActivePlatformByRulebaseID(ruleBaseID);
    }

    @Override
    public PlatformRuntime findRunningPlatformRuntime(int ruleBaseID, Date startDate) {
        return this.platformRuntimeDao.findByRuleBaseIdAndStartDate(ruleBaseID,startDate);
    }

    @Override
    public void deletePlatformRuntime(PlatformRuntime platformRuntime) {
        this.platformRuntimeDao.delete(platformRuntime);

    }


    @Override
    public void save(PlatformRuntime platformRuntime) {
        this.platformRuntimeDao.save(platformRuntime);
    }

    @Override
    public void update(PlatformRuntime platformRuntime) {
        this.platformRuntimeDao.update(platformRuntime);
    }

    @Override
    public PlatformRuntime findPlatformRuntime(long id) {
        return this.platformRuntimeDao.findOne(id);

    }
}
