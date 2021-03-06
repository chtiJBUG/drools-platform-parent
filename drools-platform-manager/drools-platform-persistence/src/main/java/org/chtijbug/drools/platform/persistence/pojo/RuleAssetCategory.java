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

@Entity
@Table(name = "rule_asset_category")
@Cacheable(value = true)
public class RuleAssetCategory {
    @Id
    @SequenceGenerator(name = "rule_asset_category_id_seq", sequenceName = "rule_asset_category_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_asset_category_id_seq")
    private Long id;
    private String categoryName;

    public RuleAssetCategory() {
    }

    public RuleAssetCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
