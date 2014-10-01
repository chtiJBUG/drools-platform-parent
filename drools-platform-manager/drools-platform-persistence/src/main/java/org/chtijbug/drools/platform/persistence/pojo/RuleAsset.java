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
package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rule_asset", indexes = {@Index(columnList = "assetName")})
@Cacheable(value = true)
public class RuleAsset {
    @Id
    @SequenceGenerator(name = "rule_asset_id_seq", sequenceName = "rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_asset_id_seq")
    private Long id;

    private String packageName;
    private String assetName;
    private String state;
    private String type;
    private String summary;
    private Integer versionNumber;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<RuleAssetCategory> ruleAssetCategory = new ArrayList<>();

    public RuleAsset() {
    }

    public RuleAsset(String packageName, String assetName) {
        this.packageName = packageName;
        this.assetName = assetName;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setAssetName(String name) {
        this.assetName = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<RuleAssetCategory> getRuleAssetCategory() {
        return ruleAssetCategory;
    }

    public void setRuleAssetCategory(List<RuleAssetCategory> ruleAssetCategory) {
        this.ruleAssetCategory = ruleAssetCategory;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

}
