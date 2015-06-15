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
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterProcessEndHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecutionStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class AfterProcessEndHistoryEventStrategy extends AbstractMemoryEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterProcessEndHistoryEventStrategy.class);

    @Override
    @Transactional
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {
        AfterProcessEndHistoryEvent afterProcessEndHistoryEvent = (AfterProcessEndHistoryEvent) historyEvent;

        ProcessExecution processExecution = sessionContext.getProcessExecution();
        processExecution.setEndDate(afterProcessEndHistoryEvent.getDateEvent());
        processExecution.setStopEventID(afterProcessEndHistoryEvent.getEventID());
        processExecution.setProcessExecutionStatus(ProcessExecutionStatus.JBPMSTOPPED);
        sessionContext.setProcessExecution(null);
        LOG.debug("AfterProcessEndHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterProcessEndHistoryEvent;
    }
}
