package org.chtijbug.drools.platform.backend.topic;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.backend.topic.pojo.*;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketClient;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.backend.wsclient.listener.*;
import org.chtijbug.drools.platform.entity.*;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * Date: 06/05/14
 * Time: 18:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RuntimeConnectorService implements HeartBeatListner, IsAliveListener, JMXInfosListener, LoadNewRuleVersionListener, VersionInfosListener {

    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);

    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    @Autowired
    private PlatformRuntimeRepository platformRuntimeRepository;

    @Autowired
    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    private BaseTopicData baseTopicDataToSend;

    private Semaphore sendSemaphore = new Semaphore(1);

    @MessageMapping("/update")
    public void updateRulePackage(Integer ruleBaseID, String packageVersion) throws DroolsChtijbugException {

        WebSocketClient clientSocket = webSocketSessionManager.getWebSocketClient(ruleBaseID);
        if (clientSocket == null) {
            DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("updateRulePackage-unknowrulebaseid", ruleBaseID.toString(), null);
            throw droolsChtijbugException;
        } else {
            PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
            platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.loadNewRuleVersion);
            List<PlatformRuntime> platformRuntimeList = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(ruleBaseID);
            if (platformRuntimeList.size() != 1) {
                DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("updateRulePackage-NoUniqueRuleBaseID", ruleBaseID.toString(), null);
                throw droolsChtijbugException;
            }

        }

        PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
        platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.loadNewRuleVersion);
        PlatformRuntimeDefinition instance = platformRuntimeInstanceRepository.findByRuleBaseID(ruleBaseID);
        List<DroolsResource> droolsRessourceList = instance.getDroolsRessourcesDefinition();
        if (droolsRessourceList ==null || droolsRessourceList.size()>1 || droolsRessourceList.get(0).getGuvnor_url()==null){
            DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("updateRulePackage-NotAguvnorRessource", ruleBaseID.toString(), null);
            throw droolsChtijbugException;
        }
        DroolsResource guvnorRessource = droolsRessourceList.get(0);
        PlatformResourceFile platformResourceFile = new PlatformResourceFile(guvnorRessource.getGuvnor_url(),guvnorRessource.getGuvnor_appName(),guvnorRessource.getGuvnor_packageName(),packageVersion,null,null);
        platformManagementKnowledgeBean.getResourceFileList().add(platformResourceFile);
        platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.loadNewRuleVersion);

        try {

            clientSocket.getSession().getBasicRemote().sendObject(platformManagementKnowledgeBean);
            guvnorRessource.setGuvnor_packageVersion(packageVersion);
            platformRuntimeInstanceRepository.save(instance);
        } catch (IOException e) {
            LOG.error("updateRulePackage(ruleBaseID="+ruleBaseID+",packageVersion)="+packageVersion,e);
        } catch (EncodeException e) {
            LOG.error("updateRulePackage(ruleBaseID=" + ruleBaseID + ",packageVersion)=" + packageVersion, e);
        }


    }


    @SendTo("/topic/heartbeat")
    public HeartBeatData sendHeartBeatInfo() {
        return (HeartBeatData) this.baseTopicDataToSend;
    }

    @Override
    public void messageReceived(Integer ruleBaseID, Date date) {
        try {
            sendSemaphore.acquire();
            HeartBeatData heartBeatData = (HeartBeatData) baseTopicDataToSend;
            heartBeatData.setRuleBaseID(ruleBaseID);
            heartBeatData.setLastTimeAlive(date);
            this.baseTopicDataToSend = heartBeatData;
            this.sendHeartBeatInfo();
        } catch (InterruptedException e) {
            LOG.debug(" messageReceived(Integer ruleBaseID, Date date)", e);
        } finally {
            sendSemaphore.release();
        }

    }

    @SendTo("/topic/isalive")
    public BaseTopicData sendisalive() {
        return this.baseTopicDataToSend;
    }

    @Override
    public void messageReceived(Integer ruleBaseID) {
        try {
            sendSemaphore.acquire();
            this.baseTopicDataToSend = new BaseTopicData();
            this.baseTopicDataToSend.setRuleBaseID(ruleBaseID);
            this.sendisalive();
        } catch (InterruptedException e) {
            LOG.debug(" public void messageReceived(Integer ruleBaseID)", e);
        } finally {
            sendSemaphore.release();
        }
    }

    @SendTo("/topic/rtparamters")
    public RealTimeParametersData sendrtparameters() {
        return (RealTimeParametersData) this.baseTopicDataToSend;
    }

    @Override
    public void messageReceived(Integer ruleBaseID, RealTimeParameters realTimeParameters) {
        try {
            sendSemaphore.acquire();
            RealTimeParametersData realTimeParametersData = new RealTimeParametersData();
            realTimeParametersData.setRuleBaseID(ruleBaseID);
            realTimeParametersData.setRealTimeParameters(realTimeParameters);
            this.baseTopicDataToSend = realTimeParametersData;
            this.sendrtparameters();
        } catch (InterruptedException e) {
            LOG.debug("public void messageReceived(Integer ruleBaseID, RealTimeParameters realTimeParameters) ", e);
        } finally {
            sendSemaphore.release();
        }
    }

    @SendTo("/topic/newpackageVersiondeployed")
    public NewPackageVersionDeployedData sendNewPackageVersionDeployed() {
        return (NewPackageVersionDeployedData) this.baseTopicDataToSend;
    }

    @Override
    public void messageReceived(Integer ruleBaseID, RequestStatus state, List<PlatformResourceFile> platformResourceFiles) {

        try {
            sendSemaphore.acquire();
            NewPackageVersionDeployedData newPackageVersionDeployedData = new NewPackageVersionDeployedData();
            newPackageVersionDeployedData.setRuleBaseID(ruleBaseID);
            newPackageVersionDeployedData.setState(state);
            newPackageVersionDeployedData.setPlatformResourceFiles(platformResourceFiles);
            this.baseTopicDataToSend = newPackageVersionDeployedData;
            this.sendNewPackageVersionDeployed();
        } catch (InterruptedException e) {
            LOG.debug("public void messageReceived(Integer ruleBaseID, RequestStatus state, List<PlatformResourceFile> platformResourceFiles) ", e);
        } finally {
            sendSemaphore.release();
        }
    }

    @SendTo("/topic/packageVersion")
    public PackageVersionInfosDeployedData sendPackageVersionInfosDeployed() {
        return (PackageVersionInfosDeployedData) this.baseTopicDataToSend;
    }

    @Override
    public void messageReceived(Integer ruleBaseID, List<PlatformResourceFile> platformResourceFiles) {
        try {
            sendSemaphore.acquire();
            PackageVersionInfosDeployedData packageVersionInfosDeployedData = new PackageVersionInfosDeployedData();
            packageVersionInfosDeployedData.setRuleBaseID(ruleBaseID);
            packageVersionInfosDeployedData.setPlatformResourceFiles(platformResourceFiles);
            this.baseTopicDataToSend = packageVersionInfosDeployedData;
            this.sendPackageVersionInfosDeployed();
        } catch (InterruptedException e) {
            LOG.debug("public void messageReceived(Integer ruleBaseID, List<PlatformResourceFile> platformResourceFiles) ", e);
        } finally {
            sendSemaphore.release();
        }
    }
}
