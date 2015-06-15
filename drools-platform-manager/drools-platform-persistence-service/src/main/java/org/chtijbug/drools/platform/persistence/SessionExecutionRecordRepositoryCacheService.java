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


import org.chtijbug.drools.platform.persistence.pojo.SessionExecutionRecord;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexSessionExecution;
import org.gridgain.grid.GridException;
import org.gridgain.grid.cache.GridCache;
import org.gridgain.grid.cache.GridCacheProjection;
import org.gridgain.grid.cache.query.GridCacheQueries;
import org.gridgain.grid.cache.query.GridCacheQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


@Component
public class SessionExecutionRecordRepositoryCacheService {
    private static Logger logger = getLogger(SessionExecutionRecordRepositoryCacheService.class);

    @Autowired
    CacheSingleton cacheSingleton;
    @Autowired
    SessionExecutionRecordRepository sessionExecutionRecordRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public SessionExecutionRecord findByIdForUI(Long id) {
        SessionExecutionRecord work = null;
        work = this.sessionExecutionRecordRepository.findOne(id);
        return work;
    }

    @Transactional
    public SessionExecutionRecord findByRuleBaseIDAndSessionIdAndEndDateIsNull(Integer ruleBaseID, Integer sessionId) {
        SessionExecutionRecord result = null;
        GridCacheProjection<Long, IndexSessionExecution> ruleFlowGroupCache = getCache();
        GridCacheQueries<Long, IndexSessionExecution> queries = ruleFlowGroupCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexSessionExecution>> qry =
                queries.createSqlQuery(IndexSessionExecution.class, "ruleBaseid= ? and sessionid = ? ");
        try {
            Collection<Map.Entry<Long, IndexSessionExecution>> queryresult = qry.execute(ruleBaseID, sessionId).get();
            if (queryresult.size() != 1) {
                result = this.sessionExecutionRecordRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(ruleBaseID, sessionId);
                if (result != null) {
                    this.save(ruleBaseID, sessionId, result);
                }
            } else {
                result = queryresult.iterator().next().getValue().getSessionExecution();
            }
        } catch (GridException e) {
            logger.error(" public SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(Integer ruleBaseID, Integer sessionId)", e);
        }
        return result;
    }


    public void save(Integer ruleBaseID, Integer sessionID, SessionExecutionRecord sessionExecution) {
        SessionExecutionRecord savedObject = null;
        try {

            savedObject = this.sessionExecutionRecordRepository.save(sessionExecution);
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridCacheProjection<Long, IndexSessionExecution> sessionExecutionCache = getCache();
        boolean newCachedObjet = false;
        try {
            IndexSessionExecution cachedObject = sessionExecutionCache.get(savedObject.getId());
            if (cachedObject != null) {
                cachedObject.setSessionExecution(savedObject);
                newCachedObjet = false;
            } else {
                newCachedObjet = true;
                cachedObject = new IndexSessionExecution();
                cachedObject.setSessionExecution(savedObject);

                cachedObject.setRulebaseid(ruleBaseID);
                cachedObject.setSessionid(sessionID);
            }
            if (savedObject.getEndDate() == null) {
                sessionExecutionCache.put(savedObject.getId(), cachedObject);
            } else {
                if (!newCachedObjet) {
                    sessionExecutionCache.remove(savedObject.getId(), cachedObject);
                }
            }


        } catch (GridException e) {
            logger.error("public void save(Integer ruleBaseID, Integer sessionID, SessionExecution sessionExecution)", e);

        }
    }

    private GridCacheProjection<Long, IndexSessionExecution> getCache() {
        GridCache<?, ?> goGridCache = cacheSingleton.getGrid().cache("local_tx_indexsessionexecution");
        GridCacheProjection<Long, IndexSessionExecution> ruleExecutionCache = goGridCache.projection(Long.class, IndexSessionExecution.class);

        return ruleExecutionCache;
    }
}
