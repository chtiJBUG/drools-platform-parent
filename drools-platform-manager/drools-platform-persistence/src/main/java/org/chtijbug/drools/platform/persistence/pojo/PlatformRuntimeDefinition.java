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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "platform_runtime_definition", indexes = {@Index(columnList = "ruleBaseID")})
@Cacheable(value = true)
public class PlatformRuntimeDefinition implements Serializable {

    @Id
    @SequenceGenerator(name = "platform_runtime_definition_id_seq", sequenceName = "platform_runtime_definition_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platform_runtime_definition_id_seq")
    private Long id;

    @Column(nullable = false)
    private Integer ruleBaseID;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "platformRuntimeDefinition", fetch = FetchType.LAZY)
    private List<PlatformRuntimeInstance> platformRuntimeInstances = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DroolsResource> droolsRessourcesDefinition = new ArrayList<>();

    private String couldInstanceStartWithNewRuleVersion;

    public PlatformRuntimeDefinition() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public List<PlatformRuntimeInstance> getPlatformRuntimeInstances() {
        return platformRuntimeInstances;
    }

    public void setPlatformRuntimeInstances(List<PlatformRuntimeInstance> platformRuntimeInstances) {
        this.platformRuntimeInstances = platformRuntimeInstances;
    }

    public List<DroolsResource> getDroolsRessourcesDefinition() {
        return droolsRessourcesDefinition;
    }

    public void setDroolsRessourcesDefinition(List<DroolsResource> droolsRessourcesDefinition) {
        this.droolsRessourcesDefinition = droolsRessourcesDefinition;
    }

    public String getCouldInstanceStartWithNewRuleVersion() {
        return couldInstanceStartWithNewRuleVersion;
    }

    public void setCouldInstanceStartWithNewRuleVersion(String couldInstanceStartWithNewRuleVersion) {
        this.couldInstanceStartWithNewRuleVersion = couldInstanceStartWithNewRuleVersion;
    }
}
