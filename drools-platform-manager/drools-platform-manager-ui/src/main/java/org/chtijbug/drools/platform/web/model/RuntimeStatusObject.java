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

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstanceStatus;

public class RuntimeStatusObject {
    private PlatformRuntimeInstanceStatus runtimeStatus;
    private String code;
    private String description;

    public RuntimeStatusObject() { /* nop */ }

    public RuntimeStatusObject(PlatformRuntimeInstanceStatus runtimeStatus, String description) {
        this.runtimeStatus = runtimeStatus;
        this.code = runtimeStatus.name();
        this.description = description;
    }

    public PlatformRuntimeInstanceStatus getRuntimeStatus() {
        return runtimeStatus;
    }

    public void setRuntimeStatus(PlatformRuntimeInstanceStatus runtimeStatus) {
        this.runtimeStatus = runtimeStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
