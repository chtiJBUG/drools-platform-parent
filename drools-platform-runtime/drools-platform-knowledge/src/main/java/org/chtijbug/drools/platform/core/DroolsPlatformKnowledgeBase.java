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
package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.HistoryContainer;
import org.chtijbug.drools.platform.core.droolslistener.PlatformHistoryListener;
import org.chtijbug.drools.platform.core.droolslistener.RuleBaseReady;
import org.chtijbug.drools.platform.core.droolslistener.SessionHistoryListener;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.JavaDialect;
import org.chtijbug.drools.runtime.impl.RuleBaseSingleton;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsResource;
import org.chtijbug.drools.runtime.resource.DrlDroolsResource;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DroolsPlatformKnowledgeBase implements DroolsPlatformKnowledgeBaseRuntime, RuleBaseReady {
    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBase.class);

    /**
     * Rule base ID (UID for the runtime
     */
    private Integer ruleBaseID;
    /**
     * Rule base singleton (Knwledge session factory)
     */
    private RuleBaseSingleton ruleBasePackage;
    /** */
    private PlatformHistoryListener historyListener;

    private List<DroolsResource> droolsResources = new ArrayList<>();
    /**
     * Instant messaging channel *
     */
    private String webSocketHostname;
    private WebSocketServerInstance webSocketServer;
    private int webSocketPort = 8025;
    private String webSocketEndPoint = "/runtime";
    /**
     * Event Messaging channel settings
     */
    private String platformServer;
    private Integer platformPort = 61616;
    private String platformQueueName = "historyEventQueue";
    /**
     * Runtime internal Status
     */
    private boolean isReady = false;

    private String guvnorUsername;
    private String guvnorPassword;
    private JavaDialect javaDialect = null;


    private ExecutorService executorService = Executors.newFixedThreadPool(5);


    public DroolsPlatformKnowledgeBase() {/* nop*/}

    public DroolsPlatformKnowledgeBase(Integer ruleBaseID, List<DroolsResource> droolsResources, JavaDialect javaDialect, WebSocketServerInstance webSocketServer, PlatformHistoryListener historyListener) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketServer = webSocketServer;
        this.historyListener = historyListener;
        this.javaDialect = javaDialect;
        this.webSocketHostname = webSocketServer.getHostName();
        this.webSocketPort = webSocketServer.getPort();
        this.webSocketEndPoint = webSocketServer.getEndPoint();
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBase(Integer ruleBaseID, List<DroolsResource> droolsResources, WebSocketServerInstance webSocketServer, PlatformHistoryListener historyListener) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketServer = webSocketServer;
        this.historyListener = historyListener;
        this.webSocketHostname = webSocketServer.getHostName();
        this.webSocketPort = webSocketServer.getPort();
        initPlatformRuntime();
    }


    public void initPlatformRuntime() throws DroolsChtijbugException, InterruptedException, UnknownHostException {
        logger.debug(">>createPackageBasePackage");
        ruleBasePackage = new RuleBaseSingleton(this.ruleBaseID, RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.historyListener);
        if (javaDialect != null) {
            ruleBasePackage.setJavaDialect(this.javaDialect);
        }
        PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent = new PlatformKnowledgeBaseInitialConnectionEvent(-1, new Date(), this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setRuleBaseID(this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setSessionId(-1);
        platformKnowledgeBaseInitialConnectionEvent.setHostname(this.webSocketHostname);
        platformKnowledgeBaseInitialConnectionEvent.setPort(this.webSocketPort);
        platformKnowledgeBaseInitialConnectionEvent.setEndPoint(this.webSocketEndPoint);
        if (droolsResources.size() == 1 && droolsResources.get(0) instanceof GuvnorDroolsResource) {
            GuvnorDroolsResource guvnorDroolsResource = (GuvnorDroolsResource) droolsResources.get(0);
            GuvnorResourceFile guvnorResourceFile = new GuvnorResourceFile(guvnorDroolsResource.getBaseUrl(), guvnorDroolsResource.getWebappName(), guvnorDroolsResource.getPackageName(), guvnorDroolsResource.getPackageVersion(), guvnorDroolsResource.getUsername(), guvnorDroolsResource.getPassword());
            platformKnowledgeBaseInitialConnectionEvent.getResourceFiles().add(guvnorResourceFile);
            ruleBasePackage.setGuvnor_username(guvnorResourceFile.getGuvnor_userName());
            ruleBasePackage.setGuvnor_password(guvnorResourceFile.getGuvnor_password());
            this.guvnorUsername = guvnorResourceFile.getGuvnor_userName();
            this.guvnorPassword = guvnorResourceFile.getGuvnor_password();
            //historyListener.fireEvent(platformKnowledgeBaseInitialConnectionEvent);
        } else {
            for (DroolsResource droolsResource : droolsResources) {
                if (droolsResource instanceof DrlDroolsResource) {
                    DrlDroolsResource drlDroolsResource = (DrlDroolsResource) droolsResource;
                    DrlResourceFile drlResourceFile = new DrlResourceFile();
                    drlResourceFile.setFileName(drlDroolsResource.getFileName());
                    platformKnowledgeBaseInitialConnectionEvent.getResourceFiles().add(drlResourceFile);
                } else if (droolsResource instanceof Bpmn2DroolsResource) {
                    Bpmn2DroolsResource drlDroolsRessource = (Bpmn2DroolsResource) droolsResource;
                    DrlResourceFile drlResourceFile = new DrlResourceFile();
                    drlResourceFile.setFileName(drlDroolsRessource.getFileName());
                    platformKnowledgeBaseInitialConnectionEvent.getResourceFiles().add(drlResourceFile);
                }
            }
        }

        historyListener.fireEvent(platformKnowledgeBaseInitialConnectionEvent);
        isReady = false;
        logger.debug("<<createPackageBasePackage", ruleBasePackage);
    }


    public void shutdown() {
        this.historyListener.shutdown();
        this.historyListener = null;
        this.webSocketServer.end();
        this.webSocketServer = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.historyListener = null;
        this.webSocketServer.end();
    }

    @Override
    public RuleBaseSession createRuleBaseSession() throws DroolsChtijbugException {
        SessionHistoryListener sessionHistoryListener = new SessionHistoryListener();
        return this.createRuleBaseSession(RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, sessionHistoryListener);
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {
        SessionHistoryListener sessionHistoryListener = new SessionHistoryListener();
        return this.createRuleBaseSession(maxNumberRulesToExecute, sessionHistoryListener);
    }


    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute, HistoryListener sessionHistoryListener) throws DroolsChtijbugException {

        DroolsPlatformSession droolsPlatformSession = null;
        if (isReady) {
            RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute, sessionHistoryListener);
            droolsPlatformSession = new DroolsPlatformSession();
            droolsPlatformSession.setRuntimeWebSocketServerService(this.webSocketServer);
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

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    @Override
    public void dispose() {
        this.ruleBasePackage.dispose();
        this.shutdown();
    }

    @Override
    public void cleanup() {
        this.ruleBasePackage.cleanup();
        this.webSocketServer.end();
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public void disposePlatformRuleBaseSession(RuleBaseSession session) throws DroolsChtijbugException {
        HistoryContainer historyContainer = session.getHistoryContainer();
        session.dispose();
        PlatformKnowledgeBaseDisposeSessionEvent platformKnowledgeBaseDisposeSessionEvent = new PlatformKnowledgeBaseDisposeSessionEvent(-1, new Date(), this.ruleBaseID, historyContainer.getListHistoryEvent());
        //TODO optimize
        SendToJMSThread send = new SendToJMSThread(historyListener, platformKnowledgeBaseDisposeSessionEvent);
        executorService.submit(send);

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

    public String getWebSocketHostname() {
        return webSocketHostname;
    }

    public void setWebSocketHostname(String webSocketHostname) {
        this.webSocketHostname = webSocketHostname;
    }

    public int getWebSocketPort() {
        return webSocketPort;
    }

    public void setWebSocketPort(int webSocketPort) {
        this.webSocketPort = webSocketPort;
    }

    public String getPlatformServer() {
        return platformServer;
    }

    public void setPlatformServer(String platformServer) {
        this.platformServer = platformServer;
    }

    public Integer getPlatformPort() {
        return platformPort;
    }

    public void setPlatformPort(Integer platformPort) {
        this.platformPort = platformPort;
    }

    public String getPlatformQueueName() {
        return platformQueueName;
    }

    public void setPlatformQueueName(String platformQueueName) {
        this.platformQueueName = platformQueueName;
    }

    public List<DroolsResource> getDroolsResources() {
        return droolsResources;
    }

    public void setDroolsResources(List<DroolsResource> droolsResources) {
        this.droolsResources = droolsResources;
    }

    public String getGuvnorUsername() {
        return guvnorUsername;
    }


    public void setGuvnorUsername(String guvnorUsername) {
        this.guvnorUsername = guvnorUsername;
    }

    public String getGuvnorPassword() {
        return guvnorPassword;
    }

    public void setGuvnorPassword(String guvnorPassword) {
        this.guvnorPassword = guvnorPassword;
    }


}
