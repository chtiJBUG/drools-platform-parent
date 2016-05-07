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

import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 21/01/14
 * Time: 18:12
 * To change this template use File | Settings | File Templates.
 */
public class PlatformKnowledgeBaseShutdownEvent extends KnowledgeBaseCreatedEvent {
    private Date endDate;

    public PlatformKnowledgeBaseShutdownEvent(Long eventID, Date dateEvent, Long ruleBaseID, Date endDate) {
        super(eventID, dateEvent, ruleBaseID);
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformKnowledgeBaseShutdownEvent{");
        sb.append("endDate=").append(endDate);
        sb.append('}');
        return sb.toString();
    }
}
