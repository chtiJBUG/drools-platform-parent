package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexRuleFlowGroup;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.cache.GridCacheProjection;
import org.gridgain.grid.cache.query.GridCacheQueries;
import org.gridgain.grid.cache.query.GridCacheQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class RuleflowGroupRepositoryCacheService {

    private static Logger logger = getLogger(RuleflowGroupRepositoryCacheService.class);

    @Autowired
    CacheSingleton cacheSingleton;


    @Autowired
    RuleflowGroupRepository ruleflowGroupRepository;

    public List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName) {
        List<RuleflowGroup> result = null;
        GridCacheProjection<Long, IndexRuleFlowGroup> ruleFlowGroupCache = getCache();
        GridCacheQueries<Long, IndexRuleFlowGroup> queries = ruleFlowGroupCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexRuleFlowGroup>> qry =
                queries.createSqlQuery(IndexRuleFlowGroup.class, "ruleBaseid = ? and sessionid = ? and processintanceid = ? and ruleflowgroupname = ?");
        try {
            Collection<Map.Entry<Long, IndexRuleFlowGroup>> queryresult = qry.execute(ruleBaseID, sessionID, processInstanceID, ruleFlowGroupName).get();
            if (queryresult.size() == 0) {
                result = this.ruleflowGroupRepository.findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(ruleBaseID, sessionID, processInstanceID, ruleFlowGroupName);
            } else {
                for (Map.Entry<Long, IndexRuleFlowGroup> item : queryresult) {
                    result.add(queryresult.iterator().next().getValue().getRuleflowGroup());
                }
            }
        } catch (GridException e) {
            logger.error(" public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName)  ", e);
        }
        return result;
    }


    public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName) {
        RuleflowGroup result = null;
        GridCacheProjection<Long, IndexRuleFlowGroup> ruleFlowGroupCache = getCache();
        GridCacheQueries<Long, IndexRuleFlowGroup> queries = ruleFlowGroupCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexRuleFlowGroup>> qry =
                queries.createSqlQuery(IndexRuleFlowGroup.class, "ruleBaseid = ? and sessionid = ? and processintanceid = ? and ruleflowgroupname = ?");
        try {
            Collection<Map.Entry<Long, IndexRuleFlowGroup>> queryresult = qry.execute(ruleBaseID, sessionID, processInstanceID, ruleFlowGroupName).get();
            if (queryresult.size() != 1) {
                result = this.ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(ruleBaseID, sessionID, processInstanceID, ruleFlowGroupName);
            } else {
                result = queryresult.iterator().next().getValue().getRuleflowGroup();
            }
        } catch (GridException e) {
            logger.error(" public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String processInstanceID, String ruleFlowGroupName)  ", e);
        }
        return result;
    }


    public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName) {
        RuleflowGroup result = null;
        GridCacheProjection<Long, IndexRuleFlowGroup> ruleFlowGroupCache = getCache();
        GridCacheQueries<Long, IndexRuleFlowGroup> queries = ruleFlowGroupCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexRuleFlowGroup>> qry =
                queries.createSqlQuery(IndexRuleFlowGroup.class, "ruleBaseid= ? and sessionid = ? and ruleflowgroupname= ?");
        try {
            Collection<Map.Entry<Long, IndexRuleFlowGroup>> queryresult = qry.execute(ruleBaseID, sessionID, ruleFlowGroupName).get();
            if (queryresult.size() != 1) {
                result = this.ruleflowGroupRepository.findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(ruleBaseID, sessionID, ruleFlowGroupName);
            } else {
                result = queryresult.iterator().next().getValue().getRuleflowGroup();
            }
        } catch (GridException e) {
            logger.error(" public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName) ", e);
        }
        return result;
    }


    public void save(Integer ruleBaseID, Integer sessionID, Integer processIntanceId, RuleflowGroup ruleflowGroup) {


        RuleflowGroup savedObject = this.ruleflowGroupRepository.save(ruleflowGroup);

        GridCacheProjection<Long, IndexRuleFlowGroup> ruleFlowgroupCache = getCache();
        boolean newCachedObjet = false;
        try {
            IndexRuleFlowGroup cachedObject = ruleFlowgroupCache.get(savedObject.getId());
            if (cachedObject != null) {
                cachedObject.setRuleflowGroup(savedObject);
                newCachedObjet = false;
            } else {
                newCachedObjet = true;
                cachedObject = new IndexRuleFlowGroup();
                cachedObject.setRuleflowGroup(savedObject);

                cachedObject.setRulebaseid(ruleBaseID);
                cachedObject.setSessionid(sessionID);
                cachedObject.setRuleflowgroupname(ruleflowGroup.getRuleflowGroup());
                cachedObject.setProcessintanceid(processIntanceId);
            }
            if (savedObject.getEndDate() == null) {
                ruleFlowgroupCache.put(savedObject.getId(), cachedObject);
            } else {
                if (!newCachedObjet) {
                    ruleFlowgroupCache.remove(savedObject.getId(), cachedObject);
                }
            }


        } catch (GridException e) {
            logger.error("public void save(Integer ruleBaseID,Integer sessionID,Integer processIntanceId,RuleflowGroup ruleflowGroup)n", e);

        }
    }

    private GridCacheProjection<Long, IndexRuleFlowGroup> getCache() {
        GridCache<?, ?> goGridCache = cacheSingleton.getGrid().cache("local_tx_indexruleflowgroup");
        GridCacheProjection<Long, IndexRuleFlowGroup> ruleExecutionCache = goGridCache.projection(Long.class, IndexRuleFlowGroup.class);

        return ruleExecutionCache;
    }
}
