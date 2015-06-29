package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.RuleFlowExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Autowired
    RuleFlowExecutionRecordRepository ruleFlowExecutionRecordRepository;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseDisposeSessionEvent event = (PlatformKnowledgeBaseDisposeSessionEvent) historyEvent;
        ThreadSaveSession threadSaveSession = new ThreadSaveSession(messageHandlerResolver, platformRuntimeInstanceRepository, sessionExecutionRepository, ruleFlowExecutionRecordRepository, event);
        executorService.execute(threadSaveSession);
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
