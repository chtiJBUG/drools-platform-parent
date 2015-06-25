package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.chtijbug.drools.platform.persistence.utility.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(PlatformKnowledgeBaseDisposeSessionEventStrategy.class);

    @Autowired
    MessageHandlerResolver messageHandlerResolver;


    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Autowired
    SessionExecutionRecordRepository sessionExecutionRepository;

    @Override
    @Transactional(value = "transactionManager")
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        long initTime = System.currentTimeMillis();
        SessionContext sessionContext = new SessionContext();
        PlatformKnowledgeBaseDisposeSessionEvent event = (PlatformKnowledgeBaseDisposeSessionEvent) historyEvent;
        logger.info("Nb Events=" + event.getSessionHistory().size());
        for (HistoryEvent historyEventElement : event.getSessionHistory()) {
            AbstractMemoryEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandlerMemory(historyEventElement);
            try {
                strategy.handleMessageInternally(historyEventElement, sessionContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long constructTimeTime = System.currentTimeMillis();
        logger.info("TimeToCreateSessionStructure=" + new Long(constructTimeTime - initTime));
        SessionExecution toSaveSession = sessionContext.getSessionExecution();

        SessionExecutionRecord sessionExecutionRecord = new SessionExecutionRecord();
        sessionExecutionRecord.setProcessingStartDate(new Date());
        sessionExecutionRecord.setStartDate(toSaveSession.getStartDate());
        sessionExecutionRecord.setSessionId(toSaveSession.getSessionId());

        sessionExecutionRecord.setSessionExecutionStatus(toSaveSession.getSessionExecutionStatus());
        sessionExecutionRecord.setEndDate(toSaveSession.getEndDate());
        sessionExecutionRecord.setProcessingStartDate(toSaveSession.getProcessingStartDate());
        sessionExecutionRecord.setProcessingStopDate(toSaveSession.getProcessingStopDate());
        sessionExecutionRecord.setEndDate(toSaveSession.getEndDate());
        sessionExecutionRecord.setPlatformRuntimeMode(toSaveSession.getPlatformRuntimeMode());

        List<PlatformRuntimeInstance> platformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(event.getRuleBaseID());

        if (platformRuntimeInstances.size() == 1) {

            PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstances.get(0);
            logger.info("foundPlatformInstsance" + platformRuntimeInstance.toString());
            sessionExecutionRecord.setPlatformRuntimeInstance(platformRuntimeInstance);
            if (platformRuntimeInstance.getPlatformRuntimeDefinition() != null && platformRuntimeInstance.getPlatformRuntimeDefinition().getPlatformRuntimeMode() != null) {
                sessionExecutionRecord.setPlatformRuntimeMode(platformRuntimeInstance.getPlatformRuntimeDefinition().getPlatformRuntimeMode());

            } else {
                sessionExecutionRecord.setPlatformRuntimeMode(PlatformRuntimeMode.Debug);
            }

            long beforeJsonTime = System.currentTimeMillis();
            try {
                long tailleString = 0;
                SessionExecution sessionExecution = sessionContext.getSessionExecution();

                for (ProcessExecution processExecution : sessionExecution.getProcessExecutions()) {


                    for (RuleflowGroup ruleflowGroup : processExecution.getRuleflowGroups()) {
                        RuleFlowExecutionRecord ruleFlowExecutionRecord = new RuleFlowExecutionRecord();
                        sessionExecutionRecord.getRuleFlowExecutionRecords().add(ruleFlowExecutionRecord);
                        String jsonStringFromJavaObject = JSONHelper.getJSONStringFromJavaObject(ruleflowGroup);
                        tailleString = tailleString + jsonStringFromJavaObject.length();
                        ruleFlowExecutionRecord.setJsonRuleFlowExecution(jsonStringFromJavaObject);
                        ruleFlowExecutionRecord.setProcessInstanceId(processExecution.getProcessInstanceId());
                        ruleFlowExecutionRecord.setRuleFlowName(ruleflowGroup.getRuleflowGroup());
                        logger.info("SizeOfJSONStringFromJavaObject(p=" + processExecution.getProcessInstanceId() + ",rfg=" + ruleflowGroup.getRuleflowGroup() + "=" + jsonStringFromJavaObject.length());
                    }
                    processExecution.getRuleflowGroups().clear();
                }
                // String jsonText = JSONHelper.getJSONStringFromJavaObject(sessionContext.getSessionExecution());
                long afterJsonTime = System.currentTimeMillis();
                logger.info("SizeOfJSONStringFromJavaObject=" + tailleString);
                logger.info("TimeTogetJSONStringFromJavaObject=" + new Long(afterJsonTime - beforeJsonTime));
                String jsonText = JSONHelper.getJSONStringFromJavaObject(sessionExecution);
                sessionExecutionRecord.setJsonSessionExecution(jsonText);
                //jsonText = null;
            } catch (Exception e) {
                logger.error("JSONHelper.getJSONStringFromJavaObjec", e);
                long afterJsonTime = System.currentTimeMillis();
                logger.info("TimeTogetJSONStringFromJavaObjectExeption=" + new Long(afterJsonTime - beforeJsonTime));
            }


            long beforeSaveTime = System.currentTimeMillis();
            sessionExecutionRepository.save(sessionExecutionRecord);
            long afterSaveTime = System.currentTimeMillis();
            logger.info("TimeToSaveObject" + new Long(afterSaveTime - beforeSaveTime));
        }
        toSaveSession = null;

        sessionContext = null;
        long endProcessTime = System.currentTimeMillis();
        logger.info("TotalProcessTime=" + new Long(endProcessTime - initTime));


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
