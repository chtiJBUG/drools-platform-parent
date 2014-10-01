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

import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.chtijbug.drools.platform.persistence.searchobjects.IndexRuleAsset;
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
public class RuleAssetRepositoryCacheService {
    private static Logger logger = getLogger(RuleAssetRepositoryCacheService.class);

    @Autowired
    CacheSingleton cacheSingleton;
    @Autowired
    RuleAssetRepository ruleAssetRepository;

    public RuleAsset findByPackageNameAndAssetName(String packageName, String assetName) {
        RuleAsset result = null;
        GridCacheProjection<Long, IndexRuleAsset> ruleAssetCache = getCache();
        GridCacheQueries<Long, IndexRuleAsset> queries = ruleAssetCache.queries();
        GridCacheQuery<Map.Entry<Long, IndexRuleAsset>> qry =
                queries.createSqlQuery(IndexRuleAsset.class, "packageName= ? and assetName = ?");
        try {
            Collection<Map.Entry<Long, IndexRuleAsset>> queryresult = qry.execute(packageName, assetName).get();
            if (queryresult.size() != 1) {
                result = this.ruleAssetRepository.findByPackageNameAndAssetName(packageName, assetName);
                if (result != null) {
                    IndexRuleAsset cachedObject = new IndexRuleAsset();
                    cachedObject.setRuleAsset(result);
                    cachedObject.setAssetName(result.getAssetName());
                    cachedObject.setPackageName(result.getPackageName());
                    ruleAssetCache.put(result.getId(), cachedObject);
                }
            } else {
                result = queryresult.iterator().next().getValue().getRuleAsset();
            }
        } catch (GridException e) {
            logger.error("public RuleAsset findByPackageNameAndAssetName(String packageName, String assetName)", e);
        }
        return result;
    }


    public void save(RuleAsset ruleAsset) {
        RuleAsset savedObject = this.ruleAssetRepository.save(ruleAsset);
        GridCacheProjection<Long, IndexRuleAsset> ruleAssetCache = getCache();
        try {
            IndexRuleAsset cachedObject = ruleAssetCache.get(savedObject.getId());
            if (cachedObject != null) {
                cachedObject.setRuleAsset(savedObject);
            } else {
                cachedObject = new IndexRuleAsset();
                cachedObject.setRuleAsset(savedObject);
                cachedObject.setAssetName(ruleAsset.getAssetName());
                cachedObject.setPackageName(ruleAsset.getPackageName());
                ruleAssetCache.put(savedObject.getId(), cachedObject);
            }
        } catch (GridException e) {
            logger.error("public void save(RuleAsset ruleAsset)", e);

        }
    }

    private GridCacheProjection<Long, IndexRuleAsset> getCache() {
        GridCache<?, ?> goGridCache = cacheSingleton.getGrid().cache("local_tx_indexruleasset");
        GridCacheProjection<Long, IndexRuleAsset> ruleAssetCache = goGridCache.projection(Long.class, IndexRuleAsset.class);

        return ruleAssetCache;
    }
}
