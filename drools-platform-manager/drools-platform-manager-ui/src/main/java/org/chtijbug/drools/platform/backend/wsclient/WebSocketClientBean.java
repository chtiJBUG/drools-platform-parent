package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.backend.AppContext;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.RealTimeParametersRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.springframework.context.ApplicationContext;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 21/01/14
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */

@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClientBean
        extends Endpoint {
    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);


    PlatformRuntimeRepository platformRuntimeRepository;


    SessionRuntimeRepository sessionRuntimeRepository;


    RealTimeParametersRepository realTimeParametersRepository;


    private PlatformRuntime platformRuntime;

    private Session peerLoggerClient;

    public WebSocketClientBean(PlatformRuntime platformRuntime) {
        this.platformRuntime = platformRuntime;
        ApplicationContext applicationContext = AppContext.getApplicationContext();
        this.platformRuntimeRepository = applicationContext.getBean(PlatformRuntimeRepository.class);
        this.sessionRuntimeRepository = applicationContext.getBean(SessionRuntimeRepository.class);
        this.realTimeParametersRepository = applicationContext.getBean(RealTimeParametersRepository.class);
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
        this.peerLoggerClient.getBasicRemote().sendObject(bean);
    }


    @Override
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        this.peerLoggerClient = session;
        session.addMessageHandler(new MessageHandler.Whole<PlatformManagementKnowledgeBean>() {
             @Override
            public void onMessage(PlatformManagementKnowledgeBean bean) {
                if (bean.getRequestStatus() == RequestStatus.FAILURE) {
                    LOG.error("WebSocket Failure Response", bean.getDroolsChtijbugException());
                } else {
                    switch (bean.getRequestRuntimePlarform()) {
                        case jmxInfos:
                            RealTimeParameters realTimeParameters = new RealTimeParameters();
                            PlatformRuntime targetplatformRuntime = platformRuntimeRepository.findByRuleBaseID(platformRuntime.getRuleBaseID());
                            realTimeParameters.setPlatformRuntime(targetplatformRuntime);
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
                            break;
                        case versionInfos:
                            break;
                        case isAlive:
                            break;
                        case ruleVersionInfos:
                            break;
                        case loadNewRuleVersion:
                            break;
                    }
                }
            }
        });
    }
}
