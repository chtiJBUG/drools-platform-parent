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

import java.util.Date;


public class FireAllRulesExecution {


    private Date startDate;
    private Date endDate;

    private Long nbreRulesFired;
    private Long maxNbreRulesDefinedForSession;

    private Long executionTime;


    private Integer startEventID;


    private FireAllRulesExecutionStatus fireAllRulesExecutionStatus;


    private Integer stopEventID;

    private Integer maxRulesEventID;

    public FireAllRulesExecution() {
    }



    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStartEventID() {
        return startEventID;
    }

    public void setStartEventID(Integer startEventID) {
        this.startEventID = startEventID;
    }



    public Long getNbreRulesFired() {
        return nbreRulesFired;
    }

    public void setNbreRulesFired(Long nbreRulesFired) {
        this.nbreRulesFired = nbreRulesFired;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public FireAllRulesExecutionStatus getFireAllRulesExecutionStatus() {
        return fireAllRulesExecutionStatus;
    }

    public void setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus fireAllRulesExecutionStatus) {
        this.fireAllRulesExecutionStatus = fireAllRulesExecutionStatus;
    }

    public Long getMaxNbreRulesDefinedForSession() {
        return maxNbreRulesDefinedForSession;
    }

    public void setMaxNbreRulesDefinedForSession(Long maxNbreRulesDefinedForSession) {
        this.maxNbreRulesDefinedForSession = maxNbreRulesDefinedForSession;
    }


    public Integer getStopEventID() {
        return stopEventID;
    }

    public void setStopEventID(Integer stopEventID) {
        this.stopEventID = stopEventID;
    }

    public Integer getMaxRulesEventID() {
        return maxRulesEventID;
    }

    public void setMaxRulesEventID(Integer maxRulesEventID) {
        this.maxRulesEventID = maxRulesEventID;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FireRulesRuntime{");
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", nbreRulesFired=").append(nbreRulesFired);
        sb.append(", maxNbreRulesDefinedForSession=").append(maxNbreRulesDefinedForSession);
        sb.append(", executionTime=").append(executionTime);
        sb.append(", startEventID=").append(startEventID);
        sb.append(", fireRulesRuntimeStatus=").append(fireAllRulesExecutionStatus);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FireAllRulesExecution)) return false;

        FireAllRulesExecution that = (FireAllRulesExecution) o;


        if (!startDate.equals(that.startDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        return result;
    }

}
