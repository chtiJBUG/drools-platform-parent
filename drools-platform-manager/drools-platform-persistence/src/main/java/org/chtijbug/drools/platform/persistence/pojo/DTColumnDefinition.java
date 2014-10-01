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
@Table(name = "dt_columndefinition_rule_asset")
public class DTColumnDefinition {
    @Id
    @SequenceGenerator(name = "dt_columndefinition_rule_asset_id_seq", sequenceName = "dt_columndefinition_rule_asset_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dt_columndefinition_rule_asset_id_seq")
    private Long id;

    private String name;

    private String dataType;

    public DTColumnDefinition() {
    }

    public DTColumnDefinition(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
