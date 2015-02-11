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
package org.chtijbug.drools.platform.runtime.servlet;

import com.google.common.base.Throwables;
import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBaseRuntime;
import org.chtijbug.drools.platform.runtime.servlet.historylistener.ServletJmsStorageHistoryListener;
import org.chtijbug.drools.platform.runtime.servlet.websocket.SpringWebSocketServer;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.JavaDialect;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


@Component
public class DroolsPlatformKnowledgeBaseJavaEE implements DroolsPlatformKnowledgeBaseRuntime {

    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBaseJavaEE.class);
    /**
     * Rule base ID (UID for the runtime
     */
    private Long ruleBaseID;
    /**
     * Rule base singleton (Knowledge session factory)
     */
    private DroolsPlatformKnowledgeBaseRuntime ruleBasePackage;


    private Semaphore startAndGo = new Semaphore(1);

    /** */


    private ServletJmsStorageHistoryListener servletJmsStorageHistoryListener;
    /** */
    private List<KnowledgeResource> droolsResources = new ArrayList<>();
    /**
     * Instant messaging channel *
     */
    private String webSocketHostname;


    private SpringWebSocketServer webSocketServer;

    private String webSocketEndPoint;

    private int webSocketPort = 8080;
    /**
     * Event Messaging channel settings
     */
    private String platformServer;

    private Integer platformPort = 61616;

    private String platformQueueName = "historyEventQueue";

    private JavaDialect javaDialect = null;
    private String groupId;
    private String artifactId;
    private String version;
    private String username;
    private String password;

    public DroolsPlatformKnowledgeBaseJavaEE() {
    }

    public DroolsPlatformKnowledgeBaseJavaEE(Long ruleBaseID,String groupId,String artifactId,String version, List<KnowledgeResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer, JavaDialect javaDialect) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.groupId=groupId;
        this.artifactId=artifactId;
        this.version = version;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.platformServer = platformServer;
        this.javaDialect = javaDialect;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaEE(Long ruleBaseID,String groupId,String artifactId,String version, List<KnowledgeResource> droolsResources,
                                             String webSocketHostname, int webSocketPort,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.groupId=groupId;
        this.artifactId=artifactId;
        this.version = version;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.webSocketPort = webSocketPort;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaEE(Long ruleBaseID,String groupId,String artifactId,String version, List<KnowledgeResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.groupId=groupId;
        this.artifactId=artifactId;
        this.version = version;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public void initPlatformRuntime() {
        logger.debug(">>createPackageBasePackage");
        logger.debug("<<<createPackageBasePackage");
    }

    public void startConnectionToPlatform() {
        try {
            startAndGo.acquire();
            if (this.webSocketServer != null && this.servletJmsStorageHistoryListener != null) {
                ruleBasePackage = new DroolsPlatformKnowledgeBase(this.ruleBaseID, this.droolsResources,  this.webSocketServer, this.servletJmsStorageHistoryListener,this.groupId,this.artifactId,this.version);
            }
            startAndGo.release();
        } catch (DroolsChtijbugException | InterruptedException | UnknownHostException e) {
            logger.error("Error while initialisazing caused by {}", e);
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<<<createPackageBasePackage");

        }
    }

    public void setServletJmsStorageHistoryListener(ServletJmsStorageHistoryListener servletJmsStorageHistoryListener) {
        try {
            startAndGo.acquire();
            this.servletJmsStorageHistoryListener = servletJmsStorageHistoryListener;
            startAndGo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void setWebSocketServer(SpringWebSocketServer webSocketServer) {
        try {
            startAndGo.acquire();
            this.webSocketServer = webSocketServer;
            startAndGo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
    public void RecreateKBaseWithNewResources(List<KnowledgeResource> res)  {
        ruleBasePackage.RecreateKBaseWithNewResources(res);
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
    public boolean isReady() {
        return this.ruleBasePackage.isReady();
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

    public void setEndPoint(String endPoint) {
        this.webSocketEndPoint = endPoint;
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

    public String getWebSocketHostname() {
        return webSocketHostname;
    }

    public int getWebSocketPort() {
        return webSocketPort;
    }

    public String getWebSocketEndPoint() {
        return webSocketEndPoint;
    }

    public String getPlatformServer() {
        return platformServer;
    }

    public List<KnowledgeResource> getDroolsResources() {
        return droolsResources;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
