/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.ResourceFile;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketClient;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.chtijbug.drools.platform.persistence.DeploymentHostRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeDefinitionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.PlatformServerRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Component
public class PlatformKnowledgeBaseInitialConnectionEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(PlatformKnowledgeBaseInitialConnectionEventStrategy.class);

    @Autowired
    WebSocketSessionManager webSocketSessionManager;


    @Autowired
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;

    @Autowired
    PlatformRuntimeDefinitionRepositoryCacheService platformRuntimeDefinitionRepository;

    @Autowired
    PlatformServerRepositoryCacheService platformServerRepository;

    @Autowired
    DeploymentHostRepositoryCacheService deploymentHostRepository;


    @Value(value = "${runtimeSiteTopology.guvnorUserName}")
    private String guvnorUserName;

    @Value(value = "${runtimeSiteTopology.guvnorPassword}")
    private String guvnorPassword;

    @Override
    @Transactional(value = "transactionManager")
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent = (PlatformKnowledgeBaseInitialConnectionEvent) historyEvent;
        int ruleBaseId = platformKnowledgeBaseInitialConnectionEvent.getRuleBaseID();
        WebSocketClient existingWebSocketClient1 = webSocketSessionManager.getWebSocketClient(ruleBaseId);
        if (existingWebSocketClient1 == null || webSocketSessionManager.isAlive(ruleBaseId) == false) {
            if (existingWebSocketClient1 != null) {
                try {
                    existingWebSocketClient1.closeSession();
                    webSocketSessionManager.removeClient(ruleBaseId);
                } catch (IOException e) {
                    LOG.debug("could not close session", e);
                }
            }
            WebSocketClient webSocketClient = null;
            PlatformRuntimeDefinition platformRuntimeDefinition = null;
            PlatformRuntimeInstance platformRuntimeInstance = null;
            platformRuntimeDefinition = platformRuntimeDefinitionRepository.findByRuleBaseID(ruleBaseId);
            if (platformRuntimeDefinition != null) {
                List<PlatformRuntimeInstance> instances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(ruleBaseId);
                for (PlatformRuntimeInstance existingPlatformRuntimeInstance : instances) {
                    if (existingPlatformRuntimeInstance != null && existingPlatformRuntimeInstance.getEndDate() == null) {
                        existingPlatformRuntimeInstance.setEndDate(new Date());
                        existingPlatformRuntimeInstance.setShutdowDate(new Date());
                        existingPlatformRuntimeInstance.setStatus(PlatformRuntimeInstanceStatus.CRASHED);
                        /**
                         * In case a runtime instance is started without having beeing stop correctly
                         * So we have to set the previous drools ressources as ended
                         */
                        for (DroolsResource existingResource : existingPlatformRuntimeInstance.getDroolsRessources()) {
                            existingResource.setEndDate(platformKnowledgeBaseInitialConnectionEvent.getDateEvent());
                            existingResource.setStopEventID(platformKnowledgeBaseInitialConnectionEvent.getEventID());
                        }
                        //TODO : save platformRuntimeInstanceRepository.save(existingPlatformRuntimeInstance);
                    }
                }
                //TODO : if saving the instance, do not save next.
                platformRuntimeDefinitionRepository.save(platformRuntimeDefinition);
            } else {
                platformRuntimeDefinition = new PlatformRuntimeDefinition();
                platformRuntimeDefinition.setRuleBaseID(ruleBaseId);
                for (ResourceFile resourceFile : platformKnowledgeBaseInitialConnectionEvent.getResourceFiles()) {
                    DroolsResource droolsResource = new DroolsResource();
                    if (resourceFile instanceof DrlResourceFile) {
                        DrlResourceFile drlResourceFile = (DrlResourceFile) resourceFile;
                        droolsResource = new DroolsResource(drlResourceFile.getFileName(), drlResourceFile.getContent());
                        droolsResource.setStartEventID(platformKnowledgeBaseInitialConnectionEvent.getEventID());
                        platformRuntimeDefinition.getDroolsRessourcesDefinition().add(droolsResource);
                    } else if (resourceFile instanceof GuvnorResourceFile) {
                        GuvnorResourceFile guvnorResourceFile = (GuvnorResourceFile) resourceFile;
                        List<PlatformServer> platformServers = platformServerRepository.findAll();
                        if (platformServers.size() == 0) {
                            PlatformServer platformServer = new PlatformServer(guvnorResourceFile.getGuvnor_url(), guvnorResourceFile.getGuvnor_appName(), guvnorUserName, guvnorPassword);
                            platformRuntimeDefinition.setPlatformServer(platformServer);
                        }
                        if (platformServers.size() == 1) {
                            platformRuntimeDefinition.setPlatformServer(platformServers.get(0));
                        }
                        DeploymentHost deploymentHost = deploymentHostRepository.findByHostnameAndPort(platformKnowledgeBaseInitialConnectionEvent.getHostname(), platformKnowledgeBaseInitialConnectionEvent.getPort());
                        if (deploymentHost == null) {
                            deploymentHost = new DeploymentHost(platformKnowledgeBaseInitialConnectionEvent.getHostname(), platformKnowledgeBaseInitialConnectionEvent.getPort(), "tomcat", "tomcat");
                        }
                        platformRuntimeDefinition.setDeploymentHost(deploymentHost);
                        platformRuntimeDefinition.setWebsocketEndpoint(platformKnowledgeBaseInitialConnectionEvent.getEndPoint());
                        platformRuntimeDefinition.setWebsocketPort(platformKnowledgeBaseInitialConnectionEvent.getPort());
                        droolsResource = new DroolsResource(guvnorResourceFile.getGuvnor_url(), guvnorResourceFile.getGuvnor_appName(), guvnorResourceFile.getGuvnor_packageName(), guvnorResourceFile.getGuvnor_packageVersion());
                        droolsResource.setStartEventID(platformKnowledgeBaseInitialConnectionEvent.getEventID());
                        platformRuntimeDefinition.getDroolsRessourcesDefinition().add(droolsResource);
                    }
                }


            }
            platformRuntimeDefinition.setWebsocketEndpoint(platformKnowledgeBaseInitialConnectionEvent.getHostname());
            platformRuntimeInstance = new PlatformRuntimeInstance();
            platformRuntimeInstance.setRuleBaseID(platformKnowledgeBaseInitialConnectionEvent.getRuleBaseID());
            platformRuntimeInstance.setStartEventID(platformKnowledgeBaseInitialConnectionEvent.getEventID());
            platformRuntimeInstance.setStartDate(platformKnowledgeBaseInitialConnectionEvent.getStartDate());
            platformRuntimeInstance.setPlatformRuntimeDefinition(platformRuntimeDefinition);
            platformRuntimeInstance.setStatus(PlatformRuntimeInstanceStatus.INITMODE);
            platformRuntimeInstance.setPlatformRuntimeDefinition(platformRuntimeDefinition);


            try {
                webSocketClient = webSocketSessionManager.AddClient(platformRuntimeInstance);
                PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
                for (DroolsResource droolsResource : platformRuntimeDefinition.getDroolsRessourcesDefinition()) {
                    if (droolsResource.getGuvnor_url() != null) {
                        PlatformResourceFile platformResourceFile = new PlatformResourceFile(droolsResource.getGuvnor_url(), droolsResource.getGuvnor_appName(), droolsResource.getGuvnor_packageName(), droolsResource.getGuvnor_packageVersion(), null, null);
                        platformManagementKnowledgeBean.getResourceFileList().add(platformResourceFile);
                    } else {
                        PlatformResourceFile platformResourceFile = new PlatformResourceFile(droolsResource.getFileName(), droolsResource.getFileContent());
                        platformManagementKnowledgeBean.getResourceFileList().add(platformResourceFile);

                    }
                }
                platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.loadNewRuleVersion);
                platformManagementKnowledgeBean.setRequestStatus(RequestStatus.SUCCESS);
                webSocketClient.sendMessage(platformManagementKnowledgeBean);
                platformRuntimeInstance.setStatus(PlatformRuntimeInstanceStatus.STARTED);
            } catch (DeploymentException | IOException e) {
                platformRuntimeInstance.setStatus(PlatformRuntimeInstanceStatus.NOT_JOINGNABLE);
                LOG.error(" handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent) ", e);
            } catch (EncodeException e) {
                platformRuntimeInstance.setStatus(PlatformRuntimeInstanceStatus.CRASHED);
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

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        return true;
    }
}
