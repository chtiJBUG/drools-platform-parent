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
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBaseRuntime;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.runtime.javase.historylistener.JmsStorageHistoryListener;
import org.chtijbug.drools.platform.runtime.javase.websocket.WebSocketServer;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.JavaDialect;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.resource.DroolsResource;
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
    private Integer ruleBaseID;
    /**
     * Rule base singleton (Knwledge session factory)
     */
    private DroolsPlatformKnowledgeBaseRuntime ruleBasePackage;
    /** */
    private JmsStorageHistoryListener jmsStorageHistoryListener;
    /** */
    private WebSocketServerInstance runtimeWebSocketServerService;

    private List<DroolsResource> droolsResources = new ArrayList<>();
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

    private JavaDialect javaDialect = null;


    /**
     * classLoader for osgi
     */
    private ClassLoader projectClassLoader = null;


    public DroolsPlatformKnowledgeBaseJavaSE() {
    }

    public DroolsPlatformKnowledgeBaseJavaSE(Integer ruleBaseID, List<DroolsResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer, JavaDialect javaDialect) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.platformServer = platformServer;
        this.javaDialect = javaDialect;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaSE(Integer ruleBaseID, List<DroolsResource> droolsResources,
                                             String webSocketHostname, int webSocketPort,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.webSocketPort = webSocketPort;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaSE(Integer ruleBaseID, List<DroolsResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
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
            ruleBasePackage = new DroolsPlatformKnowledgeBase(this.ruleBaseID, this.droolsResources, this.javaDialect, this.webSocketServer, this.jmsStorageHistoryListener);
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
        return this.createRuleBaseSession(maxNumberRulesToExecute);
    }
    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute, HistoryListener sessionHistoryListener) throws DroolsChtijbugException {
        return ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute,sessionHistoryListener);
    }
    @Override
    public void createKBase(DroolsResource... res) throws DroolsChtijbugException {
        ruleBasePackage.createKBase(res);
    }

    @Override
    public void createKBase(List<DroolsResource> res) throws DroolsChtijbugException {
        ruleBasePackage.createKBase(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(DroolsResource... res) throws DroolsChtijbugException {
        ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(List<DroolsResource> res) throws DroolsChtijbugException {
        ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void ReloadWithSameRessources() throws DroolsChtijbugException {
        ruleBasePackage.ReloadWithSameRessources();
    }

    @Override
    public HistoryListener getHistoryListener() {
        return ruleBasePackage.getHistoryListener();
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
        ruleBasePackage.dispose();
    }

    @Override
    public void cleanup() {
        ruleBasePackage.dispose();
    }

    @Override
    public boolean isReady() {
        return this.ruleBasePackage.isReady();
    }

    @Override
    public void disposePlatformRuleBaseSession(RuleBaseSession session) throws DroolsChtijbugException {
        this.ruleBasePackage.disposePlatformRuleBaseSession(session);
    }

    @Override
    public void sendPlatformKnowledgeBaseInitialConnectionEventToServer() throws DroolsChtijbugException {
        this.ruleBasePackage.sendPlatformKnowledgeBaseInitialConnectionEventToServer();
    }

    @Override
    public List<DroolsResource> getDroolsResources() {
        return this.droolsResources;
    }

    public void setDroolsResources(List<DroolsResource> droolsResources) {
        this.droolsResources = droolsResources;
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

    public ClassLoader getProjectClassLoader() {
        return projectClassLoader;
    }

    public void setProjectClassLoader(ClassLoader projectClassLoader) {
        this.projectClassLoader = projectClassLoader;
    }
}
