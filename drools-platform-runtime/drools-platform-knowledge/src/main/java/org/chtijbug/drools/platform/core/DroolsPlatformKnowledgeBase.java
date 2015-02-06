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

import org.chtijbug.drools.runtime.resource.FileKnowledgeResource;
import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.runtime.resource.WorkbenchKnowledgeResource;
import org.chtijbug.drools.platform.core.droolslistener.PlatformHistoryListener;
import org.chtijbug.drools.platform.core.droolslistener.RuleBaseReady;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseInitialConnectionEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.RuleBaseSingleton;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;


public class DroolsPlatformKnowledgeBase implements DroolsPlatformKnowledgeBaseRuntime, RuleBaseReady {
    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBase.class);
    private List<KnowledgeResource> droolsResources;

    /**
     * Rule base ID (UID for the runtime
     */
    private Long ruleBaseID;
    /**
     * Rule base singleton (Knwledge session factory)
     */
    private RuleBaseSingleton ruleBasePackage;
    /** */
    private PlatformHistoryListener historyListener;

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
    private String groupId;
    private String artifactId;
    private String version;
    private String guvnorUsername;
    private String guvnorPassword;

    public DroolsPlatformKnowledgeBase() {/* nop*/}



    public DroolsPlatformKnowledgeBase(Long ruleBaseID, List<KnowledgeResource> droolsResources, WebSocketServerInstance webSocketServer, PlatformHistoryListener historyListener,String groupId,String artifactId,String version) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketServer = webSocketServer;
        this.historyListener = historyListener;
        this.webSocketHostname = webSocketServer.getHostName();
        this.webSocketPort = webSocketServer.getPort();
        this.groupId = groupId;
        this.artifactId=artifactId;
        this.version = version;
        initPlatformRuntime();
    }


    public void initPlatformRuntime() throws DroolsChtijbugException, InterruptedException, UnknownHostException {
        logger.debug(">>createPackageBasePackage");
        ruleBasePackage = new RuleBaseSingleton(this.ruleBaseID, RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.historyListener,this.groupId,this.artifactId,this.version);

        PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent = new PlatformKnowledgeBaseInitialConnectionEvent(-1, new Date(), this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setRuleBaseID(this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setSessionId(new Long(-1));
        platformKnowledgeBaseInitialConnectionEvent.setHostname(this.webSocketHostname);
        platformKnowledgeBaseInitialConnectionEvent.setPort(this.webSocketPort);
        platformKnowledgeBaseInitialConnectionEvent.setEndPoint(this.webSocketEndPoint);
        if (droolsResources.size() == 1 && droolsResources.get(0) instanceof WorkbenchKnowledgeResource) {
            WorkbenchKnowledgeResource droolsResource = (WorkbenchKnowledgeResource)droolsResources.get(0);
            platformKnowledgeBaseInitialConnectionEvent.getKnowledgeResources().add(droolsResource);
            this.guvnorUsername = droolsResource.getUserName();
            this.guvnorPassword = droolsResource.getPassword();
            //historyListener.fireEvent(platformKnowledgeBaseInitialConnectionEvent);
        } else {
            for (KnowledgeResource droolsResource : droolsResources) {
                if (droolsResource instanceof FileKnowledgeResource) {
                    FileKnowledgeResource drlResourceFile = new FileKnowledgeResource();
                    platformKnowledgeBaseInitialConnectionEvent.getKnowledgeResources().add(drlResourceFile);
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
        DroolsPlatformSession droolsPlatformSession = null;
        if (isReady) {
            RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession();
            droolsPlatformSession = new DroolsPlatformSession();
            droolsPlatformSession.setRuntimeWebSocketServerService(this.webSocketServer);
            droolsPlatformSession.setRuleBaseStatefulSession(created);
        }
        return droolsPlatformSession;
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {
        DroolsPlatformSession droolsPlatformSession = null;
        if (isReady) {
            RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute);
            droolsPlatformSession = new DroolsPlatformSession();
            droolsPlatformSession.setRuntimeWebSocketServerService(this.webSocketServer);
            droolsPlatformSession.setRuleBaseStatefulSession(created);
        }
        return droolsPlatformSession;
    }

    @Override
    public void loadKBase(String version) throws DroolsChtijbugException {
        this.ruleBasePackage.loadKBase(version);
    }





    @Override
    public HistoryListener getHistoryListener() {
        return this.ruleBasePackage.getHistoryListener();
    }

    @Override
    public Long getRuleBaseID() {
        return this.ruleBaseID;
    }

    @Override
    public void dispose() {
        this.ruleBasePackage.dispose();
        this.shutdown();
    }

    @Override
    public void RecreateKBaseWithNewResources(List<KnowledgeResource> droolsResources) {
        this.ruleBasePackage.RecreateKBaseWithNewResources(droolsResources);
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

    public void setRuleBaseID(Long ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
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

    public List<KnowledgeResource> getDroolsResources() {
        return droolsResources;
    }

    public void setDroolsResources(List<KnowledgeResource> droolsResources) {
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
