package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PlatformKnowledgeBaseCreatedEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(PlatformKnowledgeBaseCreatedEventStrategy.class);
    @Autowired
    WebSocketSessionManager webSocketSessionManager;

    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent = (PlatformKnowledgeBaseCreatedEvent) historyEvent;
        int ruleBaseId = platformKnowledgeBaseCreatedEvent.getRuleBaseID();
        String hostname = platformKnowledgeBaseCreatedEvent.getHostname();
        PlatformRuntime existingPlatformRuntime = null;
        /**
         * then look if exists somewhere else
         */
        try {
            existingPlatformRuntime = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(ruleBaseId);
            if (existingPlatformRuntime != null) {
                existingPlatformRuntime.setEndDate(new Date());
                existingPlatformRuntime.setStatus(PlatformRuntimeStatus.CRASHED);
                platformRuntimeRepository.save(existingPlatformRuntime);
            }
        } catch (Exception e) {
            LOG.error("could not saved " + existingPlatformRuntime.toString(), e);
        }


        PlatformRuntime platformRuntime = new PlatformRuntime();
        platformRuntime.setRuleBaseID(platformKnowledgeBaseCreatedEvent.getRuleBaseID());
        platformRuntime.setStatus(PlatformRuntimeStatus.STARTED);
        platformRuntime.setEventID(platformKnowledgeBaseCreatedEvent.getEventID());
        platformRuntime.setStartDate(platformKnowledgeBaseCreatedEvent.getStartDate());
        platformRuntime.setHostname(platformKnowledgeBaseCreatedEvent.getHostname());
        platformRuntime.setPort(platformKnowledgeBaseCreatedEvent.getPort());
        try {
            webSocketSessionManager.AddClient(platformRuntime);

        } catch (DeploymentException | IOException e) {
            platformRuntime.setStatus(PlatformRuntimeStatus.NOT_JOINGNABLE);
            LOG.error(" handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) ", e);
        }
        platformRuntimeRepository.save(platformRuntime);
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseCreatedEvent;
    }
}
