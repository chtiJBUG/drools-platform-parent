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
public class ExecutionStats {

    private String duration;
    private Integer firedRulesCount;
    private String status;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getFiredRulesCount() {
        return firedRulesCount;
    }

    public void setFiredRulesCount(Integer firedRulesCount) {
        this.firedRulesCount = firedRulesCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ExecutionStats duplicate() {
        ExecutionStats newElement = new ExecutionStats();
        newElement.setDuration(this.duration);
        newElement.setFiredRulesCount(this.firedRulesCount);
        newElement.setStatus(this.status);
        return newElement;
    }
}

