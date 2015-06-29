package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionCreatedEvent;
import org.chtijbug.drools.entity.history.session.SessionDisposedEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.RuleFlowExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.chtijbug.drools.platform.persistence.utility.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by nheron on 29/06/15.
 */
public class ThreadSaveSession implements Runnable {


    private static Logger logger = LoggerFactory.getLogger(ThreadSaveSession.class);
    private MessageHandlerResolver messageHandlerResolver;


    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;


    private SessionExecutionRecordRepository sessionExecutionRepository;


    private RuleFlowExecutionRecordRepository ruleFlowExecutionRecordRepository;

    private PlatformKnowledgeBaseDisposeSessionEvent event;

    public ThreadSaveSession(MessageHandlerResolver messageHandlerResolver, PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository, SessionExecutionRecordRepository sessionExecutionRepository, RuleFlowExecutionRecordRepository ruleFlowExecutionRecordRepository, PlatformKnowledgeBaseDisposeSessionEvent event) {
        this.messageHandlerResolver = messageHandlerResolver;
        this.platformRuntimeInstanceRepository = platformRuntimeInstanceRepository;
        this.sessionExecutionRepository = sessionExecutionRepository;
        this.ruleFlowExecutionRecordRepository = ruleFlowExecutionRecordRepository;
        this.event = event;
    }

    @Override
    public void run() {

        long initTime = System.currentTimeMillis();
        List<PlatformRuntimeInstance> platformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(event.getRuleBaseID());

        if (platformRuntimeInstances.size() == 1) {

            PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstances.get(0);
            logger.info("foundPlatformInstsance" + platformRuntimeInstance.toString());

            PlatformRuntimeMode isDebugMode;
            if (platformRuntimeInstance.getPlatformRuntimeDefinition() != null && platformRuntimeInstance.getPlatformRuntimeDefinition().getPlatformRuntimeMode() != null) {
                isDebugMode = platformRuntimeInstance.getPlatformRuntimeDefinition().getPlatformRuntimeMode();
            } else {
                isDebugMode = PlatformRuntimeMode.Debug;
            }
            long beforeJsonTime = System.currentTimeMillis();
            if (isDebugMode == PlatformRuntimeMode.Debug) {
                SessionExecutionRecord sessionExecutionRecord = new SessionExecutionRecord();
                sessionExecutionRecord.setPlatformRuntimeMode(isDebugMode);
                sessionExecutionRecord.setPlatformRuntimeInstance(platformRuntimeInstance);
                SessionContext sessionContext = new SessionContext();
                logger.info("Nb Events=" + event.getSessionHistory().size());
                Queue<HistoryEvent> eventQueues = event.getSessionHistory();
                while (!eventQueues.isEmpty()) {
                    HistoryEvent historyEventElement = eventQueues.poll();
                    AbstractMemoryEventHandlerStrategy strategy = messageHandlerResolver.resolveMessageHandlerMemory(historyEventElement);
                    try {
                        strategy.handleMessageInternally(historyEventElement, sessionContext);
                        historyEventElement = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                long constructTimeTime = System.currentTimeMillis();
                logger.info("TimeToCreateSessionStructure=" + new Long(constructTimeTime - initTime));
                SessionExecution toSaveSession = sessionContext.getSessionExecution();


                sessionExecutionRecord.setProcessingStartDate(new Date());
                sessionExecutionRecord.setStartDate(toSaveSession.getStartDate());
                sessionExecutionRecord.setSessionId(toSaveSession.getSessionId());

                sessionExecutionRecord.setSessionExecutionStatus(toSaveSession.getSessionExecutionStatus());
                sessionExecutionRecord.setEndDate(toSaveSession.getEndDate());
                sessionExecutionRecord.setProcessingStartDate(toSaveSession.getProcessingStartDate());
                sessionExecutionRecord.setProcessingStopDate(toSaveSession.getProcessingStopDate());
                sessionExecutionRecord.setEndDate(toSaveSession.getEndDate());
                sessionExecutionRecord.setPlatformRuntimeMode(toSaveSession.getPlatformRuntimeMode());

                SessionExecution sessionExecution = sessionContext.getSessionExecution();
                LinkedList<ProcessExecution> processExecutions = new LinkedList<>();
                sessionExecutionRepository.save(sessionExecutionRecord);
                try {
                    long tailleString = 0;


                    for (ProcessExecution processExecution : sessionExecution.getProcessExecutions()) {
                        Queue<RuleflowGroup> groupQueue = processExecution.getRuleflowGroups();
                        while (!groupQueue.isEmpty()) {
                            RuleflowGroup ruleflowGroup = groupQueue.poll();
                            RuleFlowExecutionRecord ruleFlowExecutionRecord = new RuleFlowExecutionRecord();

                            String jsonStringFromJavaObject = JSONHelper.getJSONStringFromJavaObject(ruleflowGroup);
                            tailleString = tailleString + jsonStringFromJavaObject.length();
                            ruleFlowExecutionRecord.setJsonRuleFlowExecution(jsonStringFromJavaObject);
                            ruleFlowExecutionRecord.setProcessInstanceId(processExecution.getProcessInstanceId());
                            ruleFlowExecutionRecord.setRuleFlowName(ruleflowGroup.getRuleflowGroup());
                            ruleFlowExecutionRecord.setSessionExecutionRecord(sessionExecutionRecord);
                            sessionExecutionRecord.getRuleFlowExecutionRecords().add(ruleFlowExecutionRecord);
                            ruleFlowExecutionRecordRepository.save(ruleFlowExecutionRecord);

                            logger.info("SizeOfJSONStringFromJavaObject(p=" + processExecution.getProcessInstanceId() + ",rfg=" + ruleflowGroup.getRuleflowGroup() + "=" + jsonStringFromJavaObject.length());
                            ruleflowGroup = null;
                        }
                        processExecution.getRuleflowGroups().clear();
                    }
                    // String jsonText = JSONHelper.getJSONStringFromJavaObject(sessionContext.getSessionExecution());
                    long afterJsonTime = System.currentTimeMillis();
                    logger.info("SizeOfJSONStringFromJavaObject=" + tailleString);
                    logger.info("TimeTogetJSONStringFromJavaObject=" + new Long(afterJsonTime - beforeJsonTime));
                    String jsonText = JSONHelper.getJSONStringFromJavaObject(sessionExecution);
                    sessionExecutionRecord.setJsonSessionExecution(jsonText);
                    long beforeSaveTime = System.currentTimeMillis();
                    sessionExecutionRepository.save(sessionExecutionRecord);
                    sessionExecutionRecord = null;
                    long afterSaveTime = System.currentTimeMillis();
                    logger.info("TimeToSaveObject" + new Long(afterSaveTime - beforeSaveTime));
                    toSaveSession = null;
                    sessionContext = null;
                    //jsonText = null;
                } catch (Exception e) {
                    logger.error("JSONHelper.getJSONStringFromJavaObjec", e);
                    long afterJsonTime = System.currentTimeMillis();
                    logger.info("TimeTogetJSONStringFromJavaObjectExeption=" + new Long(afterJsonTime - beforeJsonTime));
                }
            } else {
                SessionExecutionRecord sessionExecutionRecord = new SessionExecutionRecord();
                sessionExecutionRecord.setPlatformRuntimeMode(isDebugMode);
                sessionExecutionRecord.setPlatformRuntimeInstance(platformRuntimeInstance);
                SessionCreatedEvent sessionCreatedEvent = (SessionCreatedEvent) event.getSessionHistory().get(0);
                SessionDisposedEvent sessionDisposedEvent = (SessionDisposedEvent) event.getSessionHistory().get(event.getSessionHistory().size() - 1);
                sessionExecutionRecord.setProcessingStartDate(new Date());
                sessionExecutionRecord.setStartDate(sessionCreatedEvent.getDateEvent());
                sessionExecutionRecord.setSessionId(sessionCreatedEvent.getSessionId());

                sessionExecutionRecord.setSessionExecutionStatus(SessionExecutionStatus.DISPOSED);
                sessionExecutionRecord.setEndDate(sessionDisposedEvent.getDateEvent());
                sessionExecutionRecord.setProcessingStartDate(sessionCreatedEvent.getDateEvent());
                sessionExecutionRecord.setProcessingStopDate(sessionDisposedEvent.getDateEvent());
                sessionExecutionRecord.setEndDate(sessionDisposedEvent.getDateEvent());
                sessionExecutionRecord.setPlatformRuntimeMode(PlatformRuntimeMode.Info);
                sessionExecutionRepository.save(sessionExecutionRecord);
            }


        }

        long endProcessTime = System.currentTimeMillis();
        logger.info("TotalProcessTime=" + new Long(endProcessTime - initTime));


    }
}
