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
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecutionStatus;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Component
public class BeforeProcessStartEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeProcessStartEventStrategy.class);
    @Autowired
    ProcessExecutionRepositoryCacheService processExecutionRepository;
    @Autowired
    private SessionExecutionRepositoryCacheService sessionExecutionRepository;


    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        BeforeProcessStartHistoryEvent beforeProcessStartHistoryEvent = (BeforeProcessStartHistoryEvent) historyEvent;
        List<ProcessExecution> processExecutions = processExecutionRepository.findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(beforeProcessStartHistoryEvent.getRuleBaseID(), beforeProcessStartHistoryEvent.getSessionId(), beforeProcessStartHistoryEvent.getProcessInstance().getId());
        for (ProcessExecution runningProcessExecution : processExecutions) {
            runningProcessExecution.setEndDate(new Date());
            runningProcessExecution.setProcessExecutionStatus(ProcessExecutionStatus.CRASHED);
            processExecutionRepository.save(runningProcessExecution);
        }
        SessionExecution existingSessionRutime = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());

        ProcessExecution processExecution = new ProcessExecution();
        processExecution.setSessionExecution(existingSessionRutime);
        processExecution.setProcessInstanceId(beforeProcessStartHistoryEvent.getProcessInstance().getId());
        processExecution.setProcessName(beforeProcessStartHistoryEvent.getProcessInstance().getName());
        processExecution.setProcessPackageName(beforeProcessStartHistoryEvent.getProcessInstance().getPackageName());
        processExecution.setProcessType(beforeProcessStartHistoryEvent.getProcessInstance().getType());
        processExecution.setProcessVersion(beforeProcessStartHistoryEvent.getProcessInstance().getVersion());
        processExecution.setStartEventID(beforeProcessStartHistoryEvent.getEventID());
        processExecution.setStartDate(beforeProcessStartHistoryEvent.getDateEvent());
        processExecution.setProcessExecutionStatus(ProcessExecutionStatus.JBPMSTARTED);
        processExecutionRepository.save(processExecution);
        LOG.debug("BeforeProcessStartHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeProcessStartHistoryEvent;
    }
}
