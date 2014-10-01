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
package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandre on 24/07/2014.
 */
public class RuleAssetDetails {
    private String assetName;
    private Integer versionNumber;
    private List<RuleAssetCategory> ruleAssetCategory = new ArrayList<>();

    public RuleAssetDetails() { /* nope */ }

    public RuleAssetDetails(String assetName, Integer versionNumber, List<RuleAssetCategory> ruleAssetCategory) {
        this.assetName = assetName;
        this.versionNumber = versionNumber;
        this.ruleAssetCategory = ruleAssetCategory;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public List<RuleAssetCategory> getRuleAssetCategory() {
        return ruleAssetCategory;
    }

    public void setRuleAssetCategory(List<RuleAssetCategory> ruleAssetCategory) {
        this.ruleAssetCategory = ruleAssetCategory;
    }
}
