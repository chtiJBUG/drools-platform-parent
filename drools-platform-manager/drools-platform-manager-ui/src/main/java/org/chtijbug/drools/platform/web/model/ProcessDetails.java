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

/**
 * Created by alexandre on 03/07/2014.
 */
public class ProcessDetails {
    private String processName;
    private String processExecutionStatus;
    private String processVersion;
    private String processType;

    public ProcessDetails() {/*NOP*/}

    public ProcessDetails(String processName, String processExecutionStatus, String processVersion, String processType) {
        this.processName = processName;
        this.processExecutionStatus = processExecutionStatus;
        this.processVersion = processVersion;
        this.processType = processType;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessExecutionStatus() {
        return processExecutionStatus;
    }

    public void setProcessExecutionStatus(String processExecutionStatus) {
        this.processExecutionStatus = processExecutionStatus;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }
}
