package org.chtijbug.drools.platform.backend.service;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.platform.persistence.RuntimeStorageManager;
import org.chtijbug.drools.platform.persistence.pojo.DroolsRessource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Date;

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
        String hostname = platformKnowledgeBaseCreatedEvent.getHostname();
        PlatformRuntime existingPlatformRuntime = null;
        /**
         * then look if exists somewhere else
         */
        try {
            existingPlatformRuntime = runtimeStorageManager.findRunningPlatformRuntime(ruleBaseId);
            runtimeStorageManager.deletePlatformRuntime(existingPlatformRuntime);
        } catch (Exception e) {
            //Nothing to do
        }


        PlatformRuntime platformRuntime = new PlatformRuntime();
        platformRuntime.setRuleBaseID(platformKnowledgeBaseCreatedEvent.getRuleBaseID());
        platformRuntime.setStatus(PlatformRuntimeStatus.STARTED);
        platformRuntime.setEventID(platformKnowledgeBaseCreatedEvent.getEventID());
        platformRuntime.setStartDate(new Date());
        platformRuntime.setEndDate(platformRuntime.getStartDate());
        platformRuntime.setHostname(platformKnowledgeBaseCreatedEvent.getHostname());
        platformRuntime.setPort(platformKnowledgeBaseCreatedEvent.getPort());
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
        PlatformRuntime existingPlatformRuntime = null;
        try {
            existingPlatformRuntime = runtimeStorageManager.findRunningPlatformRuntime(platformKnowledgeBaseShutdownEvent.getRuleBaseID());
            existingPlatformRuntime.setEndDate(new Date());
            runtimeStorageManager.update(existingPlatformRuntime);
        } catch (Exception e) {
            //Nothing to do
        }

    }

    public void handleMessage(KnowledgeBaseAddRessourceEvent knowledgeBaseAddRessourceEvent) {
        PlatformRuntime existingPlatformRuntime = runtimeStorageManager.findRunningPlatformRuntime(knowledgeBaseAddRessourceEvent.getRuleBaseID());

        DroolsRessource droolsRessource = null;
        if (knowledgeBaseAddRessourceEvent.getDrlRessourceFiles().size() == 0) {
            droolsRessource = new DroolsRessource(knowledgeBaseAddRessourceEvent.getGuvnor_url(), knowledgeBaseAddRessourceEvent.getGuvnor_appName(), knowledgeBaseAddRessourceEvent.getGuvnor_packageName(), knowledgeBaseAddRessourceEvent.getGuvnor_packageVersion());
        } else {
            droolsRessource = new DroolsRessource(knowledgeBaseAddRessourceEvent.getDrlRessourceFiles().get(0).getFileName(), knowledgeBaseAddRessourceEvent.getDrlRessourceFiles().get(0).getContent());
        }
        existingPlatformRuntime.getDroolsRessources().add(droolsRessource);

        runtimeStorageManager.save(existingPlatformRuntime);

    }

}

