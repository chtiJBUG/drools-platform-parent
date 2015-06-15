package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecutionRecord;
import org.chtijbug.drools.platform.persistence.utility.JSONHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by nheron on 07/06/15.
 */
@Component
public class PlatformKnowledgeBaseDisposeSessionEventStrategy extends AbstractEventHandlerStrategy {

    @Autowired
    MessageHandlerResolver messageHandlerResolver;


    @Autowired
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;

    @Autowired
    SessionExecutionRecordRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional(value = "transactionManager")
    protected void handleMessageInternally(HistoryEvent historyEvent) {

        SessionContext sessionContext = new SessionContext();
        PlatformKnowledgeBaseDisposeSessionEvent event = (PlatformKnowledgeBaseDisposeSessionEvent) historyEvent;
        for (HistoryEvent historyEventElement : event.getSessionHistory()) {
            AbstractMemoryEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandlerMemory(historyEventElement);
            try {
                strategy.handleMessageInternally(historyEventElement, sessionContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SessionExecution toSaveSession = sessionContext.getSessionExecution();
        String jsonText = JSONHelper.getJSONStringFromJavaObject(sessionContext.getSessionExecution());

        SessionExecutionRecord sessionExecutionRecord = new SessionExecutionRecord();
        sessionExecutionRecord.setProcessingStartDate(new Date());
        sessionExecutionRecord.setStartDate(toSaveSession.getStartDate());
        sessionExecutionRecord.setSessionId(toSaveSession.getSessionId());
        sessionExecutionRecord.setJsonSessionExecution(jsonText);
        sessionExecutionRecord.setSessionExecutionStatus(toSaveSession.getSessionExecutionStatus());
        sessionExecutionRecord.setEndDate(toSaveSession.getEndDate());
        sessionExecutionRecord.setProcessingStartDate(toSaveSession.getProcessingStartDate());
        sessionExecutionRecord.setProcessingStopDate(toSaveSession.getProcessingStopDate());
        sessionExecutionRecord.setEndDate(toSaveSession.getEndDate());
        sessionExecutionRecord.setPlatformRuntimeMode(toSaveSession.getPlatformRuntimeMode());

        List<PlatformRuntimeInstance> platformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(event.getRuleBaseID());
        if (platformRuntimeInstances.size() == 1) {
            PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstances.get(0);
            sessionExecutionRecord.setPlatformRuntimeInstance(platformRuntimeInstance);
            if (platformRuntimeInstance.getPlatformRuntimeDefinition() != null && platformRuntimeInstance.getPlatformRuntimeDefinition().getPlatformRuntimeMode() != null) {
                sessionExecutionRecord.setPlatformRuntimeMode(platformRuntimeInstance.getPlatformRuntimeDefinition().getPlatformRuntimeMode());
            } else {
                sessionExecutionRecord.setPlatformRuntimeMode(PlatformRuntimeMode.Debug);
            }
            sessionExecutionRepository.save(event.getRuleBaseID(), event.getSessionId(), sessionExecutionRecord);
        }


    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseDisposeSessionEvent;
    }

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        return true;
    }
}
