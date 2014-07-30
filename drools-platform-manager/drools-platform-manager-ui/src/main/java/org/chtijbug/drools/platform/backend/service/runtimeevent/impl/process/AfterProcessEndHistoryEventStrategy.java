package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterProcessEndHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.ProcessExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.chtijbug.drools.platform.persistence.pojo.ProcessExecutionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    ProcessExecutionRepositoryCacheService processExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterProcessEndHistoryEvent afterProcessEndHistoryEvent = (AfterProcessEndHistoryEvent) historyEvent;

        ProcessExecution processExecution = processExecutionRepository.findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(afterProcessEndHistoryEvent.getRuleBaseID(), afterProcessEndHistoryEvent.getSessionId(), afterProcessEndHistoryEvent.getProcessInstance().getId());
        processExecution.setEndDate(afterProcessEndHistoryEvent.getDateEvent());
        processExecution.setStopEventID(afterProcessEndHistoryEvent.getEventID());
        processExecution.setProcessExecutionStatus(ProcessExecutionStatus.JBPMSTOPPED);
        processExecutionRepository.save(processExecution);
        LOG.debug("AfterProcessEndHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterProcessEndHistoryEvent;
    }
}
