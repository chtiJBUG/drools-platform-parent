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


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexPlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexRuleAsset;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexSessionExecution;
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

@Component
public class PlatformRuntimeDefinitionRepositoryCacheService {


    private static Logger logger = getLogger(PlatformRuntimeDefinitionRepositoryCacheService.class);

    @Autowired
    CacheSingleton cacheSingleton;

    @Autowired
    PlatformRuntimeDefinitionRepository platformRuntimeDefinitionRepository;

    public List<PlatformRuntimeDefinition> findAll(){
       return platformRuntimeDefinitionRepository.findAll();
    }

    public PlatformRuntimeDefinition findByRuleBaseID(Integer ruleBaseId) {
        PlatformRuntimeDefinition result = null;
        GridCacheProjection<Integer, IndexPlatformRuntimeDefinition> platformInstanceDefinitionCache = getCache();
        GridCacheQueries<Integer, IndexPlatformRuntimeDefinition> queries = platformInstanceDefinitionCache.queries();
        GridCacheQuery<Map.Entry<Integer, IndexPlatformRuntimeDefinition>> qry =
                queries.createSqlQuery(IndexPlatformRuntimeDefinition.class, "ruleBaseId= ?");
        try {
            Collection<Map.Entry<Integer, IndexPlatformRuntimeDefinition>> queryresult = qry.execute(ruleBaseId).get();
            if (queryresult.size() != 1) {
                result = this.platformRuntimeDefinitionRepository.findByRuleBaseID(ruleBaseId);
                if (result != null) {
                    IndexPlatformRuntimeDefinition cachedObject = new IndexPlatformRuntimeDefinition();
                    cachedObject.setPlatformRuntimeDefinition(result);
                    cachedObject.setRuleBaseId(result.getRuleBaseID());
                    platformInstanceDefinitionCache.put(result.getRuleBaseID(), cachedObject);
                }
            } else {
                result = queryresult.iterator().next().getValue().getPlatformRuntimeDefinition();
            }
        } catch (GridException e) {
            logger.error(" public SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(Integer ruleBaseID, Integer sessionId)", e);
        }
        return result;
    }

    public void save(PlatformRuntimeDefinition platformRuntimeDefinition) {

        PlatformRuntimeDefinition savedObject = this.platformRuntimeDefinitionRepository.save(platformRuntimeDefinition);
        GridCacheProjection<Integer, IndexPlatformRuntimeDefinition> rulePlatformInstanceDefinitionCache = getCache();
        try {
            IndexPlatformRuntimeDefinition cachedObject = rulePlatformInstanceDefinitionCache.get(savedObject.getRuleBaseID());
            if (cachedObject != null) {
                cachedObject.setPlatformRuntimeDefinition(savedObject);
            } else {
                cachedObject = new IndexPlatformRuntimeDefinition();
                cachedObject.setPlatformRuntimeDefinition(savedObject);
                cachedObject.setRuleBaseId(savedObject.getRuleBaseID());
                rulePlatformInstanceDefinitionCache.put(savedObject.getRuleBaseID(), cachedObject);
            }
        } catch (GridException e) {
            logger.error("public void save(PlatformRuntimeDefinition platformRuntimeDefinition)", e);

        }

    }


    private GridCacheProjection<Integer, IndexPlatformRuntimeDefinition> getCache() {
        GridCache<?, ?> goGridCache = cacheSingleton.getGrid().cache("local_tx_indexplatformruntimedefinition");
        GridCacheProjection<Integer, IndexPlatformRuntimeDefinition> platformRuntimeDefinitionCache = goGridCache.projection(Integer.class, IndexPlatformRuntimeDefinition.class);

        return platformRuntimeDefinitionCache;
    }
}
