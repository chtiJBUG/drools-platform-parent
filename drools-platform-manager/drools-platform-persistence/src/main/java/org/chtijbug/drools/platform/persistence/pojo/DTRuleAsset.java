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
@Table(name = "dt_rule_asset")
public class DTRuleAsset {
    @Id
    @SequenceGenerator(name = "dt_rule_asset_id_seq", sequenceName = "dt_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_rule_asset_id_seq")
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RuleAsset ruleAsset;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dtruleasset_id")
    private List<DTColumnDefinition> dtColumnDefinitions = new ArrayList<DTColumnDefinition>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "dtruleasset_id")
    private List<DTRowData> dtRowDatas = new ArrayList<DTRowData>();

    public DTRuleAsset() {
    }


    public RuleAsset getRuleAsset() {
        return ruleAsset;
    }

    public void setRuleAsset(RuleAsset ruleAsset) {
        this.ruleAsset = ruleAsset;
    }

    public List<DTColumnDefinition> getDtColumnDefinitions() {
        return dtColumnDefinitions;
    }

    public void setDtColumnDefinitions(List<DTColumnDefinition> dtColumnDefinitions) {
        this.dtColumnDefinitions = dtColumnDefinitions;
    }

    public List<DTRowData> getDtRowDatas() {
        return dtRowDatas;
    }

    public void setDtRowDatas(List<DTRowData> dtRowDatas) {
        this.dtRowDatas = dtRowDatas;
    }
}
