package org.chtijbug.drools.platform.persistence.impl.dao.impl;

import org.chtijbug.drools.platform.entity.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/02/14
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */

@Repository
public class PlatformRuntimeDao extends AbstractJpaDAO<PlatformRuntime> implements IPlatformRuntimeDao {

    public PlatformRuntimeDao() {
        setClazz(PlatformRuntime.class);
    }

    @Override
    public PlatformRuntime findbyActivePlatformByRulebaseID(int ruleBaseId) {
        return entityManager.createNamedQuery("PlatformRuntime.findbyActivePlatformByRulebaseID", PlatformRuntime.class)
                .setParameter("ruleBaseId", ruleBaseId)
                .getSingleResult();

    }

    @Override
    public PlatformRuntime findByRuleBaseIdAndStartDate(int ruleBaseId, Date startDate) {
        return entityManager.createNamedQuery("PlatformRuntime.findByRuleBaseIdAndStartDate", PlatformRuntime.class)
                .setParameter("ruleBaseId", ruleBaseId)
                .setParameter("startDate", startDate)
                .getSingleResult();
    }

    @Override
    public List<PlatformRuntime> findActiveByHostName(String hostname) {
        return entityManager.createNamedQuery("PlatformRuntime.findActiveByHostName", PlatformRuntime.class)
                .setParameter("hostname", hostname)
                .getResultList();
    }
}

