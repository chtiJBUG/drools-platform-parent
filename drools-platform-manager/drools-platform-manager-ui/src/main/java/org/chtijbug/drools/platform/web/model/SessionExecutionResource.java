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

public class SessionExecutionResource {
    private Integer ruleBaseID;
    private String rulePackage;
    private String version;
    private Integer sessionId;
    private String runtimeURL;
    private String guvnorUrl;
    private String hostname;
    private String status;
    private String StartDate;
    private String EndDate;
    private Long id;


    public SessionExecutionResource() { /*nop*/ }

    public SessionExecutionResource(Integer ruleBaseID, String rulePackage, String version, Integer sessionId, String runtimeURL, String guvnorUrl, String hostname, String status, String startDate, String endDate) {
        this.ruleBaseID = ruleBaseID;
        this.rulePackage = rulePackage;
        this.version = version;
        this.sessionId = sessionId;
        this.runtimeURL = runtimeURL;
        this.guvnorUrl = guvnorUrl;
        this.hostname = hostname;
        this.status = status;
        StartDate = startDate;
        EndDate = endDate;
    }

    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
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

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public String getRuntimeURL() {
        return runtimeURL;
    }

    public void setRuntimeURL(String runtimeURL) {
        this.runtimeURL = runtimeURL;
    }

    public String getGuvnorUrl() {
        return guvnorUrl;
    }

    public void setGuvnorUrl(String guvnorUrl) {
        this.guvnorUrl = guvnorUrl;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
