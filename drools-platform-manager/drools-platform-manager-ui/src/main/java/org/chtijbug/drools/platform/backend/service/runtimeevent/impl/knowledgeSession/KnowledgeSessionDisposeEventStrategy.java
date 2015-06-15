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
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionDisposedEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecutionStatus;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class KnowledgeSessionDisposeEventStrategy extends AbstractMemoryEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionDisposeEventStrategy.class);



    @Override
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {
        SessionDisposedEvent sessionDisposedEvent = (SessionDisposedEvent) historyEvent;
        SessionExecution existingSessionRutime = sessionContext.getSessionExecution();
        existingSessionRutime.setEndDate(sessionDisposedEvent.getDateEvent());
        existingSessionRutime.setProcessingStopDate(new Date());
        existingSessionRutime.setStopEventID(sessionDisposedEvent.getEventID());
        existingSessionRutime.setSessionExecutionStatus(SessionExecutionStatus.DISPOSED);
        LOG.debug("SessionDisposedEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionDisposedEvent;
    }

}
