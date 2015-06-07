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

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:52
 * To change this template use File | Settings | File Templates.
 */
public class PlatformKnowledgeBaseDisposeSessionEvent extends KnowledgeBaseEvent {

    private Date startDate;
    List<HistoryEvent> sessionHistory;

    public PlatformKnowledgeBaseDisposeSessionEvent(int eventID, Date dateEvent, int ruleBaseID,List<HistoryEvent> sessionHistory) {
        super(eventID, dateEvent, ruleBaseID);
        this.startDate = dateEvent;
        this.sessionHistory=sessionHistory;
    }

    public Date getStartDate() {
        return startDate;
    }

    public List<HistoryEvent> getSessionHistory() {
        return sessionHistory;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformKnowledgeBaseDisposeSessionEvent{");
        sb.append("startDate=").append(startDate);
        sb.append(", sessionHistory=").append(sessionHistory);
        sb.append('}');
        return sb.toString();
    }
}
