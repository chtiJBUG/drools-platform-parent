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
import org.chtijbug.drools.platform.persistence.SessionExecutionRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.glassfish.tyrus.client.ClientManager;
import org.springframework.context.ApplicationContext;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClient
        extends Endpoint {

    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);
    private PlatformRuntimeInstance platformRuntimeInstance;

    private Session session;
    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;
    private PlatformRuntimeDefinitionRepository platformRuntimeDefinitionRepository;
    private SessionExecutionRepository sessionExecutionRepository;
    private RealTimeParametersRepository realTimeParametersRepository;
    private Heartbeat heartbeat;
    private JMXInfosListener jmxInfosListeners;
    private HeartBeatListner heartBeatListner;
    private IsAliveListener isAliveListener;
    private LoadNewRuleVersionListener loadNewRuleVersionListener;
    private VersionInfosListener versionInfosListener;

    public WebSocketClient(PlatformRuntimeInstance platformRuntimeInstance, Heartbeat heartbeat) throws DeploymentException, IOException {
        ApplicationContext applicationContext = AppContext.getApplicationContext();
        this.platformRuntimeInstanceRepository = applicationContext.getBean(PlatformRuntimeInstanceRepository.class);
        this.sessionExecutionRepository = applicationContext.getBean(SessionExecutionRepository.class);
        this.realTimeParametersRepository = applicationContext.getBean(RealTimeParametersRepository.class);
        this.jmxInfosListeners = applicationContext.getBean(JMXInfosListener.class);
        this.heartBeatListner = applicationContext.getBean(HeartBeatListner.class);
        this.isAliveListener = applicationContext.getBean(IsAliveListener.class);
        this.loadNewRuleVersionListener = applicationContext.getBean(LoadNewRuleVersionListener.class);
        this.versionInfosListener = applicationContext.getBean(VersionInfosListener.class);
        this.platformRuntimeDefinitionRepository = applicationContext.getBean(PlatformRuntimeDefinitionRepository.class);
        this.heartbeat = heartbeat;
        this.platformRuntimeInstance = platformRuntimeInstance;
        ClientManager client = ClientManager.createClient();
        this.session = client.connectToServer(
                this,
                ClientEndpointConfig.Builder.create()
                        .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                        .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                        .build(),
                URI.create("ws://" + platformRuntimeInstance.getHostname() + ":" + platformRuntimeInstance.getPort() + platformRuntimeInstance.getEndPoint())
        );
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
                        break;

                    case loadNewRuleVersion:
                        PlatformRuntimeDefinition platformRuntimeDefinitionloadNewRuleVersion = platformRuntimeDefinitionRepository.findByRuleBaseID(platformRuntimeInstance.getRuleBaseID());
                        platformRuntimeDefinitionloadNewRuleVersion.setCouldInstanceStartWithNewRuleVersion(bean.getRequestStatus().toString());
                        platformRuntimeDefinitionRepository.save(platformRuntimeDefinitionloadNewRuleVersion);
                        loadNewRuleVersionListener.messageReceived(platformRuntimeInstance.getRuleBaseID(), bean.getRequestStatus(), bean.getResourceFileList());
                        break;
                    case heartbeat:
                        if (bean.getHeartbeat() != null) {
                            heartbeat.setLastAlive(bean.getHeartbeat().getLastAlive());
                        }
                        heartBeatListner.messageReceived(platformRuntimeInstance.getRuleBaseID(), bean.getHeartbeat().getLastAlive());
                        break;
            }
            }
        });

    }

    public void closeSession() throws IOException {
        if (this.session != null) {
            if (this.session.getMessageHandlers().size() > 0) {
                for (MessageHandler messageHandler : this.session.getMessageHandlers()) {
                    this.session.removeMessageHandler(messageHandler);
                }
            }
            this.session.close();
        }
    }

}
