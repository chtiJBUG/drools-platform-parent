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
import org.chtijbug.drools.entity.history.process.BeforeProcessStartHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecutionStatus;
import org.springframework.stereotype.Component;


@Component
public class BeforeProcessStartEventStrategy extends AbstractMemoryEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeProcessStartEventStrategy.class);

    @Override
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {
        BeforeProcessStartHistoryEvent beforeProcessStartHistoryEvent = (BeforeProcessStartHistoryEvent) historyEvent;

        ProcessExecution processExecution = new ProcessExecution();

        processExecution.setProcessInstanceId(beforeProcessStartHistoryEvent.getProcessInstance().getId());
        processExecution.setProcessName(beforeProcessStartHistoryEvent.getProcessInstance().getName());
        processExecution.setProcessPackageName(beforeProcessStartHistoryEvent.getProcessInstance().getPackageName());
        processExecution.setProcessType(beforeProcessStartHistoryEvent.getProcessInstance().getType());
        processExecution.setProcessVersion(beforeProcessStartHistoryEvent.getProcessInstance().getVersion());
        processExecution.setStartEventID(beforeProcessStartHistoryEvent.getEventID());
        processExecution.setStartDate(beforeProcessStartHistoryEvent.getDateEvent());
        processExecution.setProcessExecutionStatus(ProcessExecutionStatus.JBPMSTARTED);
        sessionContext.setProcessExecution(processExecution);
        sessionContext.getSessionExecution().getProcessExecutions().add(processExecution);
        sessionContext.getRuleflowGroups().clear();
        LOG.debug("BeforeProcessStartHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeProcessStartHistoryEvent;
    }


}
