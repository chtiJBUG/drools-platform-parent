package org.chtijbug.drools.platform.backend.service.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.ResourceFile;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketClient;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PlatformKnowledgeBaseInitialConnectionEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(PlatformKnowledgeBaseInitialConnectionEventStrategy.class);

    @Autowired
    WebSocketSessionManager webSocketSessionManager;


    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent = (PlatformKnowledgeBaseInitialConnectionEvent) historyEvent;
        int ruleBaseId = platformKnowledgeBaseInitialConnectionEvent.getRuleBaseID();
        WebSocketClient existingWebSocketClient1 = webSocketSessionManager.getWebSocketClient(ruleBaseId);
        if (existingWebSocketClient1 == null || webSocketSessionManager.isAlive(ruleBaseId) == false) {
            if (existingWebSocketClient1 != null) {
                try {
                    existingWebSocketClient1.closeSession();
                } catch (IOException e) {
                    LOG.debug("could not close session", e);
                }
            }
            WebSocketClient webSocketClient = null;
            PlatformRuntimeInstance platformRuntimeInstance = null;
            PlatformRuntime platformRuntime = null;
            platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseID(ruleBaseId);
            if (platformRuntimeInstance != null) {
                for (PlatformRuntime existingPlatformRuntime : platformRuntimeInstance.getPlatformRuntimes()) {
                    if (existingPlatformRuntime != null && existingPlatformRuntime.getEndDate() == null) {
                        existingPlatformRuntime.setEndDate(new Date());
                        existingPlatformRuntime.setShutdowDate(new Date());
                        existingPlatformRuntime.setStatus(PlatformRuntimeStatus.CRASHED);
                        platformRuntimeRepository.save(existingPlatformRuntime);
                    }
                }
            } else {
                platformRuntimeInstance = new PlatformRuntimeInstance();
                platformRuntimeInstance.setRuleBaseID(ruleBaseId);
                for (ResourceFile resourceFile : platformKnowledgeBaseInitialConnectionEvent.getResourceFiles()) {
                    DroolsResource droolsResource = new DroolsResource();
                    if (resourceFile instanceof DrlResourceFile) {
                        DrlResourceFile drlResourceFile = (DrlResourceFile) resourceFile;
                        droolsResource = new DroolsResource(drlResourceFile.getFileName(), drlResourceFile.getContent());
                        platformRuntimeInstance.getDroolsRessourcesDefinition().add(droolsResource);
                    } else if (resourceFile instanceof GuvnorResourceFile) {
                        GuvnorResourceFile guvnorResourceFile = (GuvnorResourceFile) resourceFile;
                        droolsResource = new DroolsResource(guvnorResourceFile.getGuvnor_url(), guvnorResourceFile.getGuvnor_appName(), guvnorResourceFile.getGuvnor_packageName(), guvnorResourceFile.getGuvnor_packageVersion());
                        platformRuntimeInstance.getDroolsRessourcesDefinition().add(droolsResource);
                    }
                }


            }
            platformRuntime = new PlatformRuntime();
            platformRuntime.setRuleBaseID(platformKnowledgeBaseInitialConnectionEvent.getRuleBaseID());
            platformRuntime.setEventID(platformKnowledgeBaseInitialConnectionEvent.getEventID());
            platformRuntime.setStartDate(platformKnowledgeBaseInitialConnectionEvent.getStartDate());
            platformRuntime.setHostname(platformKnowledgeBaseInitialConnectionEvent.getHostname());
            platformRuntime.setPort(platformKnowledgeBaseInitialConnectionEvent.getPort());
            platformRuntime.setPlatformRuntimeInstance(platformRuntimeInstance);
            platformRuntime.setStatus(PlatformRuntimeStatus.INITMODE);
            platformRuntimeInstance.getPlatformRuntimes().add(platformRuntime);


            try {
                webSocketClient = webSocketSessionManager.AddClient(platformRuntime);
                PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
                for (DroolsResource droolsResource : platformRuntimeInstance.getDroolsRessourcesDefinition()) {
                    if (droolsResource.getGuvnor_url() != null) {
                        PlatformResourceFile platformResourceFile = new PlatformResourceFile(droolsResource.getGuvnor_url(), droolsResource.getGuvnor_appName(), droolsResource.getGuvnor_packageName(), droolsResource.getGuvnor_packageVersion(), null, null);
                        platformManagementKnowledgeBean.getResourceFileList().add(platformResourceFile);
                    } else {
                        PlatformResourceFile platformResourceFile = new PlatformResourceFile(droolsResource.getFileName(), droolsResource.getFileContent());
                        platformManagementKnowledgeBean.getResourceFileList().add(platformResourceFile);

                    }
                }
                platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.loadNewRuleVersion);
                webSocketClient.getSession().getBasicRemote().sendObject(platformManagementKnowledgeBean);
                platformRuntime.setStatus(PlatformRuntimeStatus.STARTED);
            } catch (DeploymentException | IOException e) {
                platformRuntime.setStatus(PlatformRuntimeStatus.NOT_JOINGNABLE);
                LOG.error(" handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) ", e);
            } catch (EncodeException e) {
                platformRuntime.setStatus(PlatformRuntimeStatus.CRASHED);
                LOG.error(" handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) ", e);
                e.printStackTrace();
            } finally {
                platformRuntimeInstanceRepository.save(platformRuntimeInstance);
            }
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseInitialConnectionEvent;
    }
}
