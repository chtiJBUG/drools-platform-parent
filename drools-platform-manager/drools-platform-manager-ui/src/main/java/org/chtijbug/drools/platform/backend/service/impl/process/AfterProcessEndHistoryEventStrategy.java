package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterProcessEndHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessRuntimeRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntime;
import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntimeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class AfterProcessEndHistoryEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterProcessEndHistoryEventStrategy.class);

    @Autowired
    ProcessRuntimeRepository processRuntimeRepository;

        @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterProcessEndHistoryEvent afterProcessEndHistoryEvent = (AfterProcessEndHistoryEvent) historyEvent;

        ProcessRuntime processRuntime = processRuntimeRepository.findStartedProcessBySessionIDAndProcessInstanceId(afterProcessEndHistoryEvent.getSessionId(), afterProcessEndHistoryEvent.getProcessInstance().getId());
        processRuntime.setEndDate(afterProcessEndHistoryEvent.getDateEvent());
        processRuntime.setProcessRuntimeStatus(ProcessRuntimeStatus.JBPMSTOPPED);
        processRuntimeRepository.save(processRuntime);
        LOG.info("AfterProcessEndHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterProcessEndHistoryEvent;
    }
}
