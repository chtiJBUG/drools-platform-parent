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
package org.chtijbug.drools.platform.entity.event;

import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseEvent;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class PlatformKnowledgeBaseInitialConnectionEvent extends KnowledgeBaseEvent {
    private String hostname;
    private String endPoint = "/runtime";
    private int port;
    private Date startDate;

    public PlatformKnowledgeBaseInitialConnectionEvent(int eventID, Date dateEvent, int ruleBaseID) {
        super(eventID, dateEvent, ruleBaseID);
        this.startDate = dateEvent;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformKnowledgeBaseCreatedEvent{");
        sb.append("ws_hostname='").append(hostname).append('\'');
        sb.append(", ws_port=").append(port);
        sb.append(", date=").append(startDate);
        sb.append('}');
        return sb.toString();
    }
}
