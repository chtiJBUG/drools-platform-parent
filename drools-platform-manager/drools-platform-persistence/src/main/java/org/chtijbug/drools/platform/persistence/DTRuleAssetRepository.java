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

import org.chtijbug.drools.platform.persistence.pojo.DTRuleAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DTRuleAssetRepository extends JpaRepository<DTRuleAsset, Long> {
    @Query("select r from RuleAsset r,DTRuleAsset dt where dt.ruleAsset=r and r.packageName= :packageName and r.assetName=:assetName")
    DTRuleAsset findByPackageNameAndAssetName(@Param("packageName") String packageName, @Param("assetName") String assetName);
}
