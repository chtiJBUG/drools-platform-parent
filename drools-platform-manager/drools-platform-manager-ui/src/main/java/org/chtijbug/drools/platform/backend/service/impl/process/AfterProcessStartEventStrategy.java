package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterProcessStartHistoryEvent;
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
public class AfterProcessStartEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterProcessStartEventStrategy.class);
    @Autowired
    ProcessRuntimeRepository processRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        AfterProcessStartHistoryEvent afterProcessStartHistoryEvent = (AfterProcessStartHistoryEvent) historyEvent;
        ProcessRuntime processRuntime = processRuntimeRepository.findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(afterProcessStartHistoryEvent.getRuleBaseID(),afterProcessStartHistoryEvent.getSessionId(), afterProcessStartHistoryEvent.getProcessInstance().getId());
        processRuntime.setStartDate(afterProcessStartHistoryEvent.getDateEvent());
        processRuntime.setProcessRuntimeStatus(ProcessRuntimeStatus.JBPMSTARTED);
        processRuntimeRepository.save(processRuntime);
        LOG.info("AfterProcessStartHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterProcessStartHistoryEvent;
    }
}
