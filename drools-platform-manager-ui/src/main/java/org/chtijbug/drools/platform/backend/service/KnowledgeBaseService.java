package org.chtijbug.drools.platform.backend.service;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformRuntime;
import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.platform.persistence.RuntimeStorageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseService {

    private static final Logger LOG = Logger.getLogger(KnowledgeBaseService.class);

    @Autowired
    WebSocketSessionManager webSocketSessionManager;

    @Autowired
    RuntimeStorageManager runtimeStorageManager;

    public void handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) {
        int ruleBaseId = platformKnowledgeBaseCreatedEvent.getRuleBaseID();
        String hostname = platformKnowledgeBaseCreatedEvent.getPlatformRuntime().getHostname();
        List<PlatformRuntime> platformRuntimeList = null;
        /**
         * then look if exists somewhere else
         */
        platformRuntimeList = runtimeStorageManager.findRunningPlatformRuntime(ruleBaseId);
        if (platformRuntimeList.size() > 0) {
            for (PlatformRuntime platformRuntime : platformRuntimeList) {
                runtimeStorageManager.deletePlatformRuntime(platformRuntime.getOrientdbId());
            }
        }

        PlatformRuntime platformRuntime = new PlatformRuntime();
        platformRuntime.setRuleBaseID(platformKnowledgeBaseCreatedEvent.getRuleBaseID());
        platformRuntime.setStatus(PlatformRuntimeStatus.STARTED);
        platformRuntime.setEventID(platformKnowledgeBaseCreatedEvent.getEventID());
        platformRuntime.setStartDate(new Date());
        platformRuntime.setEndDate(platformRuntime.getStartDate());
        platformRuntime.setHostname(platformKnowledgeBaseCreatedEvent.getPlatformRuntime().getHostname());
        platformRuntime.setPort(platformKnowledgeBaseCreatedEvent.getPlatformRuntime().getPort());
        try {
            webSocketSessionManager.AddClient(platformRuntime.getHostname(), platformRuntime.getPort(), platformRuntime.getEndPoint());

        } catch (DeploymentException e) {
            platformRuntime.setStatus(PlatformRuntimeStatus.NOT_JOINGNABLE);
            LOG.error(" handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) ", e);
        } catch (IOException e) {
            platformRuntime.setStatus(PlatformRuntimeStatus.NOT_JOINGNABLE);
            LOG.error(" handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) ", e);
        }
        runtimeStorageManager.save(platformRuntime);

    }

    public void handleMessage(KnowledgeBaseInitialLoadEvent knowledgeBaseInitialLoadEvent) {
    }

    public void handleMessage(PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent) {
        List<PlatformRuntime> platformRuntimes = runtimeStorageManager.findRunningPlatformRuntime(platformKnowledgeBaseShutdownEvent.getRuleBaseID());
        for (PlatformRuntime platformRuntime : platformRuntimes) {
            platformRuntime.setEndDate(new Date());
            platformRuntime.setStatus(PlatformRuntimeStatus.STOPPED);
            runtimeStorageManager.updatePlatformRuntime(platformRuntime.getOrientdbId(), platformRuntime);


        }
    }
}
