package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.BeforeProcessStartHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntime;
import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntimeStatus;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
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
    ProcessRuntimeRepository processRuntimeRepository;
    @Autowired
    private SessionRuntimeRepository sessionRuntimeRepository;


    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        BeforeProcessStartHistoryEvent beforeProcessStartHistoryEvent = (BeforeProcessStartHistoryEvent) historyEvent;
        List<ProcessRuntime> processRuntimes = processRuntimeRepository.findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(beforeProcessStartHistoryEvent.getRuleBaseID(), beforeProcessStartHistoryEvent.getSessionId(), beforeProcessStartHistoryEvent.getProcessInstance().getId());
        for (ProcessRuntime runningProcessRuntime : processRuntimes) {
            runningProcessRuntime.setEndDate(new Date());
            runningProcessRuntime.setProcessRuntimeStatus(ProcessRuntimeStatus.CRASHED);
            processRuntimeRepository.save(runningProcessRuntime);
        }
        SessionRuntime existingSessionRutime = sessionRuntimeRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());

        ProcessRuntime processRuntime = new ProcessRuntime();
        processRuntime.setSessionRuntime(existingSessionRutime);
        processRuntime.setProcessInstanceId(beforeProcessStartHistoryEvent.getProcessInstance().getId());
        processRuntime.setProcessName(beforeProcessStartHistoryEvent.getProcessInstance().getName());
        processRuntime.setProcessPackageName(beforeProcessStartHistoryEvent.getProcessInstance().getPackageName());
        processRuntime.setProcessType(beforeProcessStartHistoryEvent.getProcessInstance().getType());
        processRuntime.setProcessVersion(beforeProcessStartHistoryEvent.getProcessInstance().getVersion());
        processRuntime.setEventID(beforeProcessStartHistoryEvent.getEventID());
        processRuntime.setStartDate(beforeProcessStartHistoryEvent.getDateEvent());
        processRuntime.setProcessRuntimeStatus(ProcessRuntimeStatus.JBPMSTARTED);
        processRuntimeRepository.save(processRuntime);
        LOG.debug("BeforeProcessStartHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeProcessStartHistoryEvent;
    }
}
