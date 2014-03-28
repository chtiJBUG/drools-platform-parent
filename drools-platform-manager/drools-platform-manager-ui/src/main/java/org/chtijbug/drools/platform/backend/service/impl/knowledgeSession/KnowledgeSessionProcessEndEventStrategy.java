package org.chtijbug.drools.platform.backend.service.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionStartProcessEndEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntimeStatus;
import org.chtijbug.drools.platform.persistence.ProcessRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntime;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class KnowledgeSessionProcessEndEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionProcessEndEventStrategy.class);

    @Autowired
    ProcessRuntimeRepository processRuntimeRepository;

    @Autowired
    SessionRuntimeRepository sessionRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionStartProcessEndEvent sessionStartProcessEndEvent = (SessionStartProcessEndEvent) historyEvent;

        List<ProcessRuntime> processRuntimes = processRuntimeRepository.findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(sessionStartProcessEndEvent.getRuleBaseID(),sessionStartProcessEndEvent.getSessionId(), sessionStartProcessEndEvent.getProcessInstanceId());
        for (ProcessRuntime runningProcessRuntime : processRuntimes){
            runningProcessRuntime.setEndDate(new Date());
            runningProcessRuntime.setProcessRuntimeStatus(ProcessRuntimeStatus.CRASHED);
            processRuntimeRepository.save(runningProcessRuntime);
        }
        SessionRuntime existingSessionRutime = sessionRuntimeRepository.findBySessionIdAndStartDateIsNull(historyEvent.getSessionId());

        ProcessRuntime processRuntime = new ProcessRuntime();
        processRuntime.setSessionRuntime(existingSessionRutime);
        processRuntime.setProcessInstanceId(sessionStartProcessEndEvent.getProcessInstanceId());
        processRuntime.setProcessName(sessionStartProcessEndEvent.getProcessName());
        processRuntime.setEventID(sessionStartProcessEndEvent.getEventID());
        processRuntime.setStartDate(sessionStartProcessEndEvent.getDateEvent());
        processRuntime.setProcessRuntimeStatus(ProcessRuntimeStatus.SESSIONSTARTED);
        processRuntimeRepository.save(processRuntime);
        LOG.info("SessionStartProcessEndEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionStartProcessEndEvent;
    }
}
