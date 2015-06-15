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
package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.backend.AppContext;
import org.chtijbug.drools.platform.backend.wsclient.listener.*;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeDefinitionRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.RealTimeParametersRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClient
        extends Endpoint {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);
    final private Heartbeat heartbeat = new Heartbeat();
    @Value(value = "${knowledge.numberRetriesConnectionToRuntime}")
    private int numberRetries;
    private PlatformRuntimeInstance platformRuntimeInstance;
    private int timeToWaitBetweenTwoRetries;
    private Session session;
    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;
    private PlatformRuntimeDefinitionRepository platformRuntimeDefinitionRepository;
    private SessionExecutionRecordRepository sessionExecutionRecordRepository;
    private RealTimeParametersRepository realTimeParametersRepository;
    private JMXInfosListener jmxInfosListeners;
    private HeartBeatListner heartBeatListner;
    private IsAliveListener isAliveListener;
    private LoadNewRuleVersionListener loadNewRuleVersionListener;
    private VersionInfosListener versionInfosListener;

    public WebSocketClient(PlatformRuntimeInstance platformRuntimeInstance, int numberRetries, int timeToWaitBetweenTwoRetries) throws DeploymentException, IOException {
        ApplicationContext applicationContext = AppContext.getApplicationContext();
        this.platformRuntimeInstanceRepository = applicationContext.getBean(PlatformRuntimeInstanceRepository.class);
        this.sessionExecutionRecordRepository = applicationContext.getBean(SessionExecutionRecordRepository.class);
        this.realTimeParametersRepository = applicationContext.getBean(RealTimeParametersRepository.class);
        this.jmxInfosListeners = applicationContext.getBean(JMXInfosListener.class);
        this.heartBeatListner = applicationContext.getBean(HeartBeatListner.class);
        this.isAliveListener = applicationContext.getBean(IsAliveListener.class);
        this.loadNewRuleVersionListener = applicationContext.getBean(LoadNewRuleVersionListener.class);
        this.versionInfosListener = applicationContext.getBean(VersionInfosListener.class);
        this.platformRuntimeDefinitionRepository = applicationContext.getBean(PlatformRuntimeDefinitionRepository.class);
        this.platformRuntimeInstance = platformRuntimeInstance;
        this.numberRetries = numberRetries;
        this.timeToWaitBetweenTwoRetries = timeToWaitBetweenTwoRetries;

        /**
         * Let us start n times teh connection + wait between timeout
         */
        boolean connected = false;
        int retryNumber = 0;
        Exception lastException = null;
        while (retryNumber < this.numberRetries && connected == false) {
            try {
                ClientManager client = ClientManager.createClient();
                String hostname = platformRuntimeInstance.getPlatformRuntimeDefinition().getDeploymentHost().getHostname();
                Integer portNumber = platformRuntimeInstance.getPlatformRuntimeDefinition().getWebsocketPort();
                String endPointName = platformRuntimeInstance.getPlatformRuntimeDefinition().getWebsocketEndpoint();
                this.session = client.connectToServer(
                        this,
                        ClientEndpointConfig.Builder.create()
                                .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                                .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                                .build(),

                        URI.create("ws://" + hostname + ":" + portNumber + endPointName)
                );
                connected = true;
                LOG.info("Successfully Connected to " + platformRuntimeInstance.toString());
            } catch (Exception e) {
                lastException = e;
                try {
                    Thread.sleep(this.timeToWaitBetweenTwoRetries);
                } catch (InterruptedException e1) {
                    LOG.error("Could not  wait  " + platformRuntimeInstance.toString() + " Try number=" + retryNumber, e1);
                }
            } finally {
                retryNumber++;
            }
        }
        if (connected == false && retryNumber >= this.numberRetries) {
            if (lastException != null) {
                LOG.error("Could not Connect to " + platformRuntimeInstance.toString() + " after =" + retryNumber, lastException);

                if (lastException instanceof IOException) {
                    throw (IOException) lastException;
                }
            }
        }
    }


    @Override
    public void onClose(Session session, CloseReason closeReason) {

        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {

        super.onError(session, thr);
    }

    public void sendMessage(PlatformManagementKnowledgeBean bean) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(bean);
    }


    @Override
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        this.session = session;

        session.addMessageHandler(new MessageHandler.Whole<PlatformManagementKnowledgeBean>() {
            @Override
            public void onMessage(PlatformManagementKnowledgeBean bean) {

                switch (bean.getRequestRuntimePlarform()) {
                    case jmxInfos:
                        RealTimeParameters realTimeParameters = new RealTimeParameters();
                        PlatformRuntimeInstance targetplatformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseID(platformRuntimeInstance.getRuleBaseID());
                        realTimeParameters.setPlatformRuntimeInstance(targetplatformRuntimeInstance);
                        JMXInfo jmxInfo = bean.getJmxInfo();
                        realTimeParameters.setAverageTimeExecution(jmxInfo.getAverageTimeExecution());
                        realTimeParameters.setMinTimeExecution(jmxInfo.getMinTimeExecution());
                        realTimeParameters.setMaxTimeExecution(jmxInfo.getMaxTimeExecution());
                        realTimeParameters.setTotalTimeExecution(jmxInfo.getTotalTimeExecution());
                        realTimeParameters.setTotalNumberRulesExecuted(jmxInfo.getTotalNumberRulesExecuted());
                        realTimeParameters.setAverageRulesExecuted(jmxInfo.getAverageRulesExecuted());
                        realTimeParameters.setMinRulesExecuted(jmxInfo.getMinRulesExecuted());
                        realTimeParameters.setMaxRulesExecuted(jmxInfo.getMaxRulesExecuted());
                        realTimeParameters.setNumberFireAllRulesExecuted(jmxInfo.getNumberFireAllRulesExecuted());
                        realTimeParameters.setAverageRuleThroughout(jmxInfo.getAverageRuleThroughout());
                        realTimeParameters.setMinRuleThroughout(jmxInfo.getMinRuleThroughout());
                        realTimeParameters.setMaxRuleThroughout(jmxInfo.getMaxRuleThroughout());
                        realTimeParametersRepository.save(realTimeParameters);
                        jmxInfosListeners.messageReceived(platformRuntimeInstance.getRuleBaseID(), realTimeParameters);
                        break;
                    case versionInfos:
                        versionInfosListener.messageReceived(platformRuntimeInstance.getRuleBaseID(), bean.getResourceFileList());
                        break;
                    case isAlive:
                        isAliveListener.messageReceived(platformRuntimeInstance.getRuleBaseID());

                        heartBeatListner.messageReceived(platformRuntimeInstance.getRuleBaseID(), bean.getHeartbeat().getLastAlive());
                        heartbeat.setLastAlive(bean.getHeartbeat().getLastAlive());
                        break;

                    case loadNewRuleVersion:
                        PlatformRuntimeDefinition platformRuntimeDefinitionloadNewRuleVersion = platformRuntimeDefinitionRepository.findByRuleBaseID(platformRuntimeInstance.getRuleBaseID());
                        platformRuntimeDefinitionloadNewRuleVersion.setCouldInstanceStartWithNewRuleVersion(bean.getRequestStatus().toString());
                        platformRuntimeDefinitionRepository.save(platformRuntimeDefinitionloadNewRuleVersion);
                        loadNewRuleVersionListener.messageReceived(platformRuntimeInstance.getRuleBaseID(), bean.getRequestStatus(), bean.getResourceFileList());
                        break;
                }
            }
        });

    }


    public Heartbeat getHeartbeat() {
        return heartbeat;
    }

    public Session getSession() {
        return this.session;
    }

    public void closeSession() throws IOException {
        if (this.session != null) {
            if (this.session.isOpen() == true) {
                if (this.session.getMessageHandlers().size() > 0) {
                    for (MessageHandler messageHandler : this.session.getMessageHandlers()) {
                        this.session.removeMessageHandler(messageHandler);
                    }
                }
                this.session.close();
            }


        }
    }

}
