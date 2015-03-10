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

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeEnvironment;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;

/**
 * Created by alexandre on 06/06/2014.
 */
public class RuntimeInstance {

    private Long id;
    private Integer ruleBaseId;
    private String url;
    private String rulePackage;
    private String version;
    private String environment;
    private String mode;
    private String status;
    public RuntimeInstance() {/* nop */}



    public RuntimeInstance(Long id, Integer ruleBaseId, String url, String rulePackage, String version, String environment, String mode, String status) {
        this.id = id;
        this.ruleBaseId = ruleBaseId;
        this.url = url;
        this.rulePackage = rulePackage;
        this.version = version;
        this.environment=environment;
        this.mode=mode;
        this.status=status;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRuleBaseId() {
        return ruleBaseId;
    }

    public void setRuleBaseId(Integer ruleBaseId) {
        this.ruleBaseId = ruleBaseId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(String rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
