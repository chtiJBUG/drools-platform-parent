package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.BeforeProcessStartHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessExecutionRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecutionStatus;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class BeforeProcessStartEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeProcessStartEventStrategy.class);
    @Autowired
    ProcessExecutionRepository processExecutionRepository;
    @Autowired
    private SessionExecutionRepository sessionExecutionRepository;


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
