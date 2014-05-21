package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.platform.core.droolslistener.JmsStorageHistoryListener;
import org.chtijbug.drools.platform.core.droolslistener.RuleBaseReady;
import org.chtijbug.drools.platform.core.websocket.RuntimeWebSocketServerService;
import org.chtijbug.drools.platform.core.websocket.WebSocketServer;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.RuleBaseSingleton;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsRessource;
import org.chtijbug.drools.runtime.resource.DrlDroolsRessource;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/12/13
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */

public class DroolsPlatformKnowledgeBase implements RuleBasePackage, RuleBaseReady {
    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBase.class);


    private Integer ruleBaseID;


    private RuleBaseSingleton ruleBasePackage;

    private JmsStorageHistoryListener jmsStorageHistoryListener;

    private RuntimeWebSocketServerService runtimeWebSocketServerService;

    private List<DroolsResource> droolsResources = new ArrayList<>();

    private String ws_hostname;

    private int ws_port=8025;
    private String platformServer;

    private Integer platformPort=61616;

    private String platformQueueName="historyEventQueue";

    private boolean isReady = false;

    private WebSocketServer webSocketServer;


    public DroolsPlatformKnowledgeBase(Integer ruleBaseID,List<DroolsResource> droolsRessourceList,
                                       String ws_hostname,
                                       String platformServer) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID=ruleBaseID;
        this.droolsResources=droolsRessourceList;
        this.ws_hostname = ws_hostname;
        this.platformServer=platformServer ;
        initPlatformRuntime();
    }

    public void setWs_port(int ws_port) {
        this.ws_port = ws_port;
    }

    public void setPlatformPort(Integer platformPort) {
        this.platformPort = platformPort;
    }

    public String getWs_hostname() {
        return ws_hostname;
    }

    public int getWs_port() {
        return ws_port;
    }

    public String getPlatformServer() {
        return platformServer;
    }

    public Integer getPlatformPort() {
        return platformPort;
    }

    public String getPlatformQueueName() {
        return platformQueueName;
    }

    public void initPlatformRuntime() throws DroolsChtijbugException, InterruptedException, UnknownHostException {
        logger.debug(">>createPackageBasePackage");
        initSocketServer();
        this.jmsStorageHistoryListener = new JmsStorageHistoryListener(this,this.platformServer,this.platformPort,this.platformQueueName);
        ruleBasePackage = new RuleBaseSingleton(RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.jmsStorageHistoryListener);
        PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent = new PlatformKnowledgeBaseInitialConnectionEvent(-1, new Date(), this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setRuleBaseID(this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setSessionId(-1);
        platformKnowledgeBaseInitialConnectionEvent.setHostname(this.ws_hostname);
        platformKnowledgeBaseInitialConnectionEvent.setPort(this.ws_port);
        if (droolsResources.size() == 1 && droolsResources.get(0) instanceof GuvnorDroolsResource) {
            GuvnorDroolsResource guvnorDroolsResource = (GuvnorDroolsResource) droolsResources.get(0);
            GuvnorResourceFile guvnorResourceFile = new GuvnorResourceFile(guvnorDroolsResource.getBaseUrl(), guvnorDroolsResource.getWebappName(), guvnorDroolsResource.getPackageName(), guvnorDroolsResource.getPackageVersion(), guvnorDroolsResource.getUsername(), guvnorDroolsResource.getPassword());
            platformKnowledgeBaseInitialConnectionEvent.getResourceFiles().add(guvnorResourceFile);
            ruleBasePackage.setGuvnor_username(guvnorResourceFile.getGuvnor_userName());
            ruleBasePackage.setGuvnor_password(guvnorResourceFile.getGuvnor_password());
            jmsStorageHistoryListener.fireEvent(platformKnowledgeBaseInitialConnectionEvent);
        } else {
            for (DroolsResource droolsResource : droolsResources) {
                if (droolsResource instanceof DrlDroolsRessource) {
                    DrlDroolsRessource drlDroolsRessource = (DrlDroolsRessource) droolsResource;
                    DrlResourceFile drlResourceFile = new DrlResourceFile();
                    drlResourceFile.setFileName(drlDroolsRessource.getFileName());
                    platformKnowledgeBaseInitialConnectionEvent.getResourceFiles().add(drlResourceFile);
                } else if (droolsResource instanceof Bpmn2DroolsRessource) {
                    Bpmn2DroolsRessource drlDroolsRessource = (Bpmn2DroolsRessource) droolsResource;
                    DrlResourceFile drlResourceFile = new DrlResourceFile();
                    drlResourceFile.setFileName(drlDroolsRessource.getFileName());
                    platformKnowledgeBaseInitialConnectionEvent.getResourceFiles().add(drlResourceFile);
                }
            }
        }

        jmsStorageHistoryListener.fireEvent(platformKnowledgeBaseInitialConnectionEvent);
        isReady = false;
        logger.debug("<<createPackageBasePackage", ruleBasePackage);
    }

    private void initSocketServer() throws UnknownHostException, InterruptedException {
        webSocketServer = new WebSocketServer(ws_hostname, ws_port, this);
        webSocketServer.run();
    }

   // @Scheduled(fixedDelay = 5000)
    public void sendHeartBeat() {
        if (this.runtimeWebSocketServerService != null) {
            this.runtimeWebSocketServerService.sendHeartBeat();
        }

    }

    public void shutdown() {
        this.jmsStorageHistoryListener.shutdown();
        this.jmsStorageHistoryListener = null;
        this.webSocketServer.end();
        this.webSocketServer = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.jmsStorageHistoryListener = null;
        this.webSocketServer.end();
    }

    private String getFileExtension(String ressourceName) {
        int mid = ressourceName.lastIndexOf(".");
        String ext = ressourceName.substring(mid + 1, ressourceName.length()).toUpperCase();
        return ext;
    }


    public List<DroolsResource> getDroolsResources() {
        return droolsResources;
    }

    public void setRuntimeWebSocketServerService(RuntimeWebSocketServerService runtimeWebSocketServerService) {
        this.runtimeWebSocketServerService = runtimeWebSocketServerService;
    }


    @Override
    public RuleBaseSession createRuleBaseSession() throws DroolsChtijbugException {
        DroolsPlatformSession droolsPlatformSession = null;
        if (isReady == true) {
            RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession();
            droolsPlatformSession = new DroolsPlatformSession();
            droolsPlatformSession.setRuntimeWebSocketServerService(this.runtimeWebSocketServerService);
            droolsPlatformSession.setRuleBaseStatefulSession(created);
        }
        return droolsPlatformSession;
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {
        DroolsPlatformSession droolsPlatformSession = null;
        if (isReady == true) {
            RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute);
            droolsPlatformSession = new DroolsPlatformSession();
            droolsPlatformSession.setRuntimeWebSocketServerService(this.runtimeWebSocketServerService);
            droolsPlatformSession.setRuleBaseStatefulSession(created);
        }
        return droolsPlatformSession;
    }

    @Override
    public void createKBase(DroolsResource... res) throws DroolsChtijbugException {
        this.ruleBasePackage.createKBase(res);
    }

    @Override
    public void createKBase(List<DroolsResource> res) throws DroolsChtijbugException {
        this.ruleBasePackage.createKBase(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(DroolsResource... res) throws DroolsChtijbugException {
        this.ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(List<DroolsResource> res) throws DroolsChtijbugException {
        this.ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void ReloadWithSameRessources() throws DroolsChtijbugException {
        this.ruleBasePackage.ReloadWithSameRessources();
    }

    @Override
    public HistoryListener getHistoryListener() {
        return this.ruleBasePackage.getHistoryListener();
    }

    @Override
    public int getRuleBaseID() {
        return this.ruleBaseID;
    }

    @Override
    public void dispose() {
        this.ruleBasePackage.dispose();
        this.shutdown();
    }

    @Override
    public void cleanup() {
        this.ruleBasePackage.cleanup();
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DroolsPlatformKnowledgeBase{");

        sb.append(", ruleBasePackage=").append(ruleBasePackage);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void setRuleBaseStatus(boolean ready) {
        this.isReady = ready;
    }
}
