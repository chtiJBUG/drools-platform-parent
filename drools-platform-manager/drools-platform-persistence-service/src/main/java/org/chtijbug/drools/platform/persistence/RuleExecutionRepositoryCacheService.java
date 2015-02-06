/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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


@Component
public class RuleExecutionRepositoryCacheService {

    private static Logger logger = getLogger(RuleExecutionRepositoryCacheService.class);

    @Autowired
    CacheSingleton cacheSingleton;

    @Autowired
    RuleExecutionRepository ruleExecutionRepository;


    public RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(Long ruleBaseID, Long sessionId, String ruleFlowGroup, String ruleName) {

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


    public RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(Long ruleBaseID, Long sessionId, String ruleName) {
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


    public void save(Long ruleBaseID, Long sessionID, String ruleFlowGroupName, RuleExecution ruleExecution) {


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
