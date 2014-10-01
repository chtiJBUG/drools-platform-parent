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
package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;


public class IndexRuleAsset implements Serializable {

    @GridCacheQuerySqlField
    private String packageName;

    @GridCacheQuerySqlField
    private String assetName;


    private RuleAsset ruleAsset;

    public IndexRuleAsset() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public RuleAsset getRuleAsset() {
        return ruleAsset;
    }

    public void setRuleAsset(RuleAsset ruleAsset) {
        this.ruleAsset = ruleAsset;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexRuleAsset{");
        sb.append("packageName='").append(packageName).append('\'');
        sb.append(", ruleName=").append(assetName);
        sb.append(", ruleAsset=").append(ruleAsset);
        sb.append('}');
        return sb.toString();
    }
}
