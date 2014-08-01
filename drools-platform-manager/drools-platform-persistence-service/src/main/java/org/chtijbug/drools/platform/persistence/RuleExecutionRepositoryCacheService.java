package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexRuleExecution;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.cache.GridCacheProjection;
import org.gridgain.grid.cache.query.GridCacheQueries;
import org.gridgain.grid.cache.query.GridCacheQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class RuleExecutionRepositoryCacheService {

    private static Logger logger = getLogger(RuleExecutionRepositoryCacheService.class);

    @Autowired
    CacheSingleton cacheSingleton;

    @Autowired
    RuleExecutionRepository ruleExecutionRepository;


    public RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleFlowGroup, String ruleName) {

        RuleExecution result = null;
        GridCacheProjection<Long, IndexRuleExecution> ruleExecutionCache = getCache();
        GridCacheQueries<Long, IndexRuleExecution> queries = ruleExecutionCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexRuleExecution>> qry =
                queries.createSqlQuery(IndexRuleExecution.class, "rulebaseid= ? and sessionid = ? and ruleflowgroup = ? and rulename= ?");
        try {
            Collection<Map.Entry<Long, IndexRuleExecution>> queryresult = qry.execute(ruleBaseID, sessionId, ruleFlowGroup, ruleName).get();
            if (queryresult.size() != 1) {
                result = this.ruleExecutionRepository.findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(ruleBaseID, sessionId, ruleFlowGroup, ruleName);
            } else {
                result = queryresult.iterator().next().getValue().getRuleExecution();

            }
        } catch (GridException e) {
            logger.error("  public RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleFlowGroup, String ruleName)) ", e);
        }
        return result;
    }


    public RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleName) {
        RuleExecution result = null;
        GridCacheProjection<Long, IndexRuleExecution> ruleExecutionCache = getCache();
        GridCacheQueries<Long, IndexRuleExecution> queries = ruleExecutionCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexRuleExecution>> qry =
                queries.createSqlQuery(IndexRuleExecution.class, "ruleBaseID= ? and sessionId = ? and ruleName= ?");
        try {
            Collection<Map.Entry<Long, IndexRuleExecution>> queryresult = qry.execute(ruleBaseID, sessionId, ruleName).get();
            if (queryresult.size() != 1) {
                result = this.ruleExecutionRepository.findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(ruleBaseID, sessionId, ruleName);
            } else {
                result = queryresult.iterator().next().getValue().getRuleExecution();
            }
        } catch (GridException e) {
            logger.error("  public RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(Integer ruleBaseID, Integer sessionId, String ruleName) ", e);
        }
        return result;
    }


    public void save(Integer ruleBaseID, Integer sessionID, String ruleFlowGroupName, RuleExecution ruleExecution) {


        RuleExecution savedObject = this.ruleExecutionRepository.save(ruleExecution);

        GridCacheProjection<Long, IndexRuleExecution> ruleExecutionCache = getCache();
        boolean newCachedObjet = false;
        try {
            IndexRuleExecution cachedObject = ruleExecutionCache.get(savedObject.getId());
            if (cachedObject != null) {
                cachedObject.setRuleExecution(savedObject);
                newCachedObjet = false;
            } else {
                newCachedObjet = true;
                cachedObject = new IndexRuleExecution();
                cachedObject.setRuleExecution(savedObject);

                cachedObject.setrulebaseid(ruleBaseID);
                cachedObject.setsessionid(sessionID);
                cachedObject.setruleflowgroup(ruleFlowGroupName);
                cachedObject.setrulename(savedObject.getRuleName());
            }
            if (savedObject.getEndDate() == null) {
                ruleExecutionCache.put(savedObject.getId(), cachedObject);
            } else {
                if (!newCachedObjet) {
                    ruleExecutionCache.remove(savedObject.getId(), cachedObject);
                }
            }


        } catch (GridException e) {
            logger.error("public void save(RuleExecution ruleExecution", e);

        }
    }

    private GridCacheProjection<Long, IndexRuleExecution> getCache() {
        GridCache<?, ?> goGridCache = cacheSingleton.getGrid().cache("local_tx_indexruleexecution");
        GridCacheProjection<Long, IndexRuleExecution> ruleExecutionCache = goGridCache.projection(Long.class, IndexRuleExecution.class);

        return ruleExecutionCache;
    }
}
