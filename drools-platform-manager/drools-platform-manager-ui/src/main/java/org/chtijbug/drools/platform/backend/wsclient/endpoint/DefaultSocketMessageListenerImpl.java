package org.chtijbug.drools.platform.backend.wsclient.endpoint;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.backend.AppContext;
import org.chtijbug.drools.platform.backend.wsclient.listener.*;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.RealTimeParametersRepository;
import org.chtijbug.drools.platform.persistence.SessionRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.springframework.context.ApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * Date: 06/05/14
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSocketMessageListenerImpl implements WebSocketMessageListener {

    private static final Logger LOG = Logger.getLogger(DefaultSocketMessageListenerImpl.class);

    private PlatformRuntimeRepository platformRuntimeRepository;
    private SessionRuntimeRepository sessionRuntimeRepository;
    private RealTimeParametersRepository realTimeParametersRepository;
    private PlatformRuntime platformRuntime;
    private PlatformManagementKnowledgeBean lastBeanReceived;
    private Heartbeat heartbeat;
    private JMXInfosListener jmxInfosListeners;
    private HeartBeatListner heartBeatListner;
    private IsAliveListener isAliveListener;
    private LoadNewRuleVersionListener loadNewRuleVersionListener;
    private VersionInfosListener versionInfosListener;

    public DefaultSocketMessageListenerImpl(PlatformRuntime platformRuntime, Heartbeat heartbeat) {
        this.platformRuntime = platformRuntime;
        ApplicationContext applicationContext = AppContext.getApplicationContext();
        this.platformRuntimeRepository = applicationContext.getBean(PlatformRuntimeRepository.class);
        this.sessionRuntimeRepository = applicationContext.getBean(SessionRuntimeRepository.class);
        this.realTimeParametersRepository = applicationContext.getBean(RealTimeParametersRepository.class);
        jmxInfosListeners = applicationContext.getBean(JMXInfosListener.class);
        heartBeatListner = applicationContext.getBean(HeartBeatListner.class);
        isAliveListener = applicationContext.getBean(IsAliveListener.class);
        loadNewRuleVersionListener = applicationContext.getBean(LoadNewRuleVersionListener.class);
        versionInfosListener = applicationContext.getBean(VersionInfosListener.class);
        this.heartbeat = heartbeat;
    }


    @Override
    public void beanReceived(PlatformManagementKnowledgeBean bean) {
        if (bean.getRequestStatus() == RequestStatus.FAILURE) {
            LOG.error("WebSocket Failure Response", bean.getDroolsChtijbugException());
        } else {
            this.lastBeanReceived = bean;
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
                    this.jmxInfosListeners.messageReceived(platformRuntime.getRuleBaseID(), realTimeParameters);
                    break;
                case versionInfos:
                    this.versionInfosListener.messageReceived(platformRuntime.getRuleBaseID(), bean.getResourceFileList());
                    break;
                case isAlive:
                    this.isAliveListener.messageReceived(platformRuntime.getRuleBaseID());
                    break;

                case loadNewRuleVersion:
                    this.loadNewRuleVersionListener.messageReceived(platformRuntime.getRuleBaseID(), bean.getRequestStatus(), bean.getResourceFileList());
                    break;
                case heartbeat:
                    if (bean.getHeartbeat() != null) {
                        this.heartbeat.setLastAlive(bean.getHeartbeat().getLastAlive());
                    }
                    this.heartBeatListner.messageReceived(platformRuntime.getRuleBaseID(), bean.getHeartbeat().getLastAlive());
                    break;
            }
        }
    }


}
