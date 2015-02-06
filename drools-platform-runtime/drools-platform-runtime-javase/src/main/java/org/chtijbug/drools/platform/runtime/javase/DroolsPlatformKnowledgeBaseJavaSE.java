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
package org.chtijbug.drools.platform.runtime.javase;

import com.google.common.base.Throwables;
import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBaseRuntime;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.runtime.javase.historylistener.JmsStorageHistoryListener;
import org.chtijbug.drools.platform.runtime.javase.websocket.WebSocketServer;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class DroolsPlatformKnowledgeBaseJavaSE implements DroolsPlatformKnowledgeBaseRuntime {

    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBaseJavaSE.class);
    /**
     * Rule base ID (UID for the runtime
     */
    private Long ruleBaseID;
    /**
     * Rule base singleton (Knwledge session factory)
     */
    private DroolsPlatformKnowledgeBaseRuntime ruleBasePackage;
    /** */
    private JmsStorageHistoryListener jmsStorageHistoryListener;
    /** */
    private WebSocketServerInstance runtimeWebSocketServerService;

    private List<KnowledgeResource> droolsResources = new ArrayList<>();
    /**
     * Instant messaging channel *
     */
    private String webSocketHostname;
    private WebSocketServerInstance webSocketServer;
    private int webSocketPort = 8025;
    /**
     * Event Messaging channel settings
     */
    private String platformServer;
    private Integer platformPort = 61616;
    private String platformQueueName = "historyEventQueue";

    private String groupId;
    private String artifactId;
    private String version;

    /**
     * classLoader for osgi
     */
    private ClassLoader projectClassLoader = null;


    public DroolsPlatformKnowledgeBaseJavaSE() {
    }



    public DroolsPlatformKnowledgeBaseJavaSE(Long ruleBaseID,String groupId,String artifactId,String version, List<KnowledgeResource> droolsResources,
                                             String webSocketHostname, int webSocketPort,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.groupId= groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.webSocketPort = webSocketPort;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaSE(Long ruleBaseID,String groupId,String artifactId,String version, List<KnowledgeResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.groupId= groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public void initPlatformRuntime() {
        logger.debug(">>createPackageBasePackage");
        try {
            webSocketServer = new WebSocketServer(webSocketHostname, webSocketPort, this);
            webSocketServer.run();
            this.jmsStorageHistoryListener = new JmsStorageHistoryListener(this, this.platformServer, this.platformPort, this.platformQueueName);
            ruleBasePackage = new DroolsPlatformKnowledgeBase(this.ruleBaseID, this.droolsResources, this.webSocketServer, this.jmsStorageHistoryListener,this.groupId,this.artifactId,this.version);
        } catch (DroolsChtijbugException | InterruptedException | UnknownHostException e) {
            logger.error("Error while initialisazing caused by {}", e);
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<<<createPackageBasePackage");

        }

    }


    @Override
    public RuleBaseSession createRuleBaseSession() throws DroolsChtijbugException {
        return ruleBasePackage.createRuleBaseSession();
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {
        return ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute);
    }

    @Override
    public void loadKBase(String version) throws DroolsChtijbugException {

    }


    @Override
    public HistoryListener getHistoryListener() {
        return ruleBasePackage.getHistoryListener();
    }

    @Override
    public Long getRuleBaseID() {
        return this.ruleBaseID;
    }

    @Override
    public void dispose() {
        ruleBasePackage.dispose();
    }

    @Override
    public void RecreateKBaseWithNewResources(List<KnowledgeResource> droolsResources) {
        this.ruleBasePackage.RecreateKBaseWithNewResources(droolsResources);
    }


    @Override
    public boolean isReady() {
        return this.ruleBasePackage.isReady();
    }



    public List<KnowledgeResource> getDroolsResources() {
        return this.droolsResources;
    }

    @Override
    public void setRuleBaseStatus(boolean b) {
        ruleBasePackage.setRuleBaseStatus(b);
    }

    @Override
    public String getGuvnorUsername() {
        return ruleBasePackage.getGuvnorUsername();
    }


    @Override
    public String getGuvnorPassword() {
        return ruleBasePackage.getGuvnorPassword();
    }

    public void setRuleBaseID(Long ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public void setWebSocketHostname(String webSocketHostname) {
        this.webSocketHostname = webSocketHostname;
    }

    public void setWebSocketPort(int webSocketPort) {
        this.webSocketPort = webSocketPort;
    }

    public void setPlatformServer(String platformServer) {
        this.platformServer = platformServer;
    }

    public void setPlatformPort(Integer platformPort) {
        this.platformPort = platformPort;
    }

    public void setDroolsResources(List<KnowledgeResource> droolsResources) {
        this.droolsResources = droolsResources;
    }

    public ClassLoader getProjectClassLoader() {
        return projectClassLoader;
    }

    public void setProjectClassLoader(ClassLoader projectClassLoader) {
        this.projectClassLoader = projectClassLoader;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


}
