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

package org.chtijbug.drools.platform.backend.websocketServer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.AppContext;
import org.chtijbug.drools.platform.backend.service.runtimeevent.MessageHandlerResolver;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.backend.wsclient.listener.*;
import org.chtijbug.drools.platform.core.wssocket.ByteUtil;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeDefinitionRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.RealTimeParametersRepository;
import org.chtijbug.drools.platform.persistence.SessionExecutionRecordRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.List;

public class SpringWebSocketServer extends TextWebSocketHandler {


    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SpringWebSocketServer.class);
    private static final Logger LOG = Logger.getLogger(SpringWebSocketServer.class);

    final private Heartbeat heartbeat = new Heartbeat();


    @Value(value = "${knowledge.numberRetriesConnectionToRuntime}")
    private String numberRetries;



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
    private MessageHandlerResolver messageHandlerResolver;
    private WebSocketSessionManager webSocketSessionManager;

    //@Value(value = "${jms.port}")
    private String jmsPort = "61616";
    //@Value(value = "${jms.server}")
    private String jmsServer = "localhost";
    private JmsTemplate jmsTemplate;
    //@Value(value = "${jms.queue}")
    private String platformQueueName;

    public SpringWebSocketServer() {
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
        this.messageHandlerResolver = applicationContext.getBean(MessageHandlerResolver.class);
        this.webSocketSessionManager = applicationContext.getBean(WebSocketSessionManager.class);
        this.jmsServer = this.webSocketSessionManager.getJmsServer();
        this.jmsPort = this.webSocketSessionManager.getJmsPort();
        this.platformQueueName = this.webSocketSessionManager.getPlatformQueueName();
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String url = "tcp://" + this.jmsServer + ":" + this.jmsPort;
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        CachingConnectionFactory cacheFactory = new CachingConnectionFactory(factory);
        this.jmsTemplate = new JmsTemplate(cacheFactory);


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessionManager.closeSession(session);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        try {
            Object obj = this.toObject(message.getPayload());
            if (obj instanceof PlatformManagementKnowledgeBean) {
                PlatformManagementKnowledgeBean bean = (PlatformManagementKnowledgeBean) obj;
                handleBean(session, bean);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode stream = new PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode();

        PlatformManagementKnowledgeBean bean = stream.decode(new StringReader(message.getPayload()));
        handleBean(session, bean);
    }

    private void handleBean(WebSocketSession session, PlatformManagementKnowledgeBean bean) throws IOException, DeploymentException {
        switch (bean.getRequestRuntimePlarform()) {
            case historyEvent:
                HistoryEvent historyEvent = bean.getHistoryEvent();
                if (historyEvent instanceof PlatformKnowledgeBaseInitialConnectionEvent) {
                    webSocketSessionManager.AddClient(historyEvent.getRuleBaseID(), session);
                }
                this.jmsTemplate.send(platformQueueName, new PlatformMessageCreator(historyEvent));
                break;
            case jmxInfos:
                RealTimeParameters realTimeParameters = new RealTimeParameters();
                List<PlatformRuntimeInstance> targetplatformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(bean.getRuleBaseId());
                if (targetplatformRuntimeInstances.size() > 0) {
                    PlatformRuntimeInstance targetplatformRuntimeInstance = targetplatformRuntimeInstances.get(0);
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
                    jmxInfosListeners.messageReceived(bean.getRuleBaseId(), realTimeParameters);
                }
                break;
            case versionInfos:
                versionInfosListener.messageReceived(bean.getRuleBaseId(), bean.getResourceFileList());
                break;
            case isAlive:
                isAliveListener.messageReceived(bean.getRuleBaseId());

                heartBeatListner.messageReceived(bean.getRuleBaseId(), bean.getHeartbeat().getLastAlive());
                heartbeat.setLastAlive(bean.getHeartbeat().getLastAlive());
                break;

            case loadNewRuleVersion:
                PlatformRuntimeDefinition platformRuntimeDefinitionloadNewRuleVersion = platformRuntimeDefinitionRepository.findByRuleBaseID(bean.getRuleBaseId());
                platformRuntimeDefinitionloadNewRuleVersion.setCouldInstanceStartWithNewRuleVersion(bean.getRequestStatus().toString());
                platformRuntimeDefinitionRepository.save(platformRuntimeDefinitionloadNewRuleVersion);
                loadNewRuleVersionListener.messageReceived(bean.getRuleBaseId(), bean.getRequestStatus(), bean.getResourceFileList());
                break;
        }
    }

    public Object toObject(ByteBuffer bytes) throws IOException, ClassNotFoundException {


        Object obj = ByteUtil.toObject(bytes.array());
        return obj;
    }
}