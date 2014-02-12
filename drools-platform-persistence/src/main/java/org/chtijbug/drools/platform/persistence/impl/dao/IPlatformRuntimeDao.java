package org.chtijbug.drools.platform.persistence.impl.dao;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/02/14
 * Time: 15:15                                                                           eID
 * To change this template use File | Settings | File Templates.
 */
public interface IPlatformRuntimeDao extends IAbstractJpaDAO<PlatformRuntime>{
    PlatformRuntime findbyActivePlatformByRulebaseID(int ruleBaseId);
    PlatformRuntime findByRuleBaseIdAndStartDate(int ruleBaseId, Date startDate) ;
    public List<PlatformRuntime> findActiveByHostName(String hostname);
}
