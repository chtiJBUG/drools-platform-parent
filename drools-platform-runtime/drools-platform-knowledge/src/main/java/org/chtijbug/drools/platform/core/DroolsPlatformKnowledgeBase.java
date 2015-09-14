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
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.core.droolslistener.PlatformHistoryListener;
import org.chtijbug.drools.platform.core.droolslistener.RuleBaseReady;
import org.chtijbug.drools.platform.core.droolslistener.SessionHistoryListener;
import org.chtijbug.drools.platform.core.wssocket.WebSocketClient;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
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

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DroolsPlatformKnowledgeBase implements DroolsPlatformKnowledgeBaseRuntime, RuleBaseReady, PlatformHistoryListener {
    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBase.class);
    public boolean connectionToServerEstablished = false;
    /**
     * Rule base ID (UID for the runtime
     */
    private Integer ruleBaseID;
    /**
     * Rule base singleton (Knwledge session factory)
     */
    private RuleBaseSingleton ruleBasePackage;
    private List<DroolsResource> droolsResources = new ArrayList<>();
    /**
     * Instant messaging channel *
     */
    private String webSocketHostname;
    private int webSocketPort = 8025;
    private String webSocketEndPoint;
    private WebSocketClient webSocketClient;

    /**
     * Runtime internal Status
     */
    private boolean isReady = false;
    private String guvnorUsername;
    private String guvnorPassword;
    private JavaDialect javaDialect = null;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private String endPoint;

    private List<HistoryEvent> notYetTransmittedEvents = new LinkedList<>();

    public DroolsPlatformKnowledgeBase() {/* nop*/}

    public DroolsPlatformKnowledgeBase(Integer ruleBaseID, List<DroolsResource> droolsResources, JavaDialect javaDialect) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.javaDialect = javaDialect;


    }

    public void setWebSocketEndPoint(String webSocketEndPoint) {
        this.webSocketEndPoint = webSocketEndPoint;
    }

    public void initPlatformRunTimeAsync() {
        WaitForServerThread waitForServerThread = new WaitForServerThread(this);
        executorService.submit(waitForServerThread);
    }

    public void initPlatformRuntime() throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        logger.debug(">>initPlatformRuntime");

        try {
            this.webSocketClient = new WebSocketClient(this.webSocketHostname, this.webSocketPort, this.webSocketEndPoint, 5, 2000, this);
        } catch (DeploymentException e) {
            logger.debug("DroolsPlatformKnowledgeBaseJavaEE.initPlatformRuntime", e);
        } catch (IOException e) {
            logger.debug("DroolsPlatformKnowledgeBaseJavaEE.initPlatformRuntime", e);
        }
        ruleBasePackage = new RuleBaseSingleton(this.ruleBaseID, RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this);
        if (javaDialect != null) {
            ruleBasePackage.setJavaDialect(this.javaDialect);
        }
        this.sendPlatformKnowledgeBaseInitialConnectionEventToServer();
        logger.debug("<<<initPlatformRuntime");
    }


    public void sendPlatformKnowledgeBaseInitialConnectionEventToServer() throws DroolsChtijbugException {
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

        this.fireEvent(platformKnowledgeBaseInitialConnectionEvent);
    }


    @Override
    public void shutdown() {

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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
            droolsPlatformSession = new DroolsPlatformSession(webSocketClient);
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
        this.executorService.shutdown();
    }

    @Override
    public void cleanup() {
        this.ruleBasePackage.cleanup();
        try {
            this.webSocketClient.closeSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isReady() {
        return isReady;
    }

    @Override
    public void disposePlatformRuleBaseSession(RuleBaseSession session) throws DroolsChtijbugException {
        HistoryContainer historyContainer = session.getHistoryContainer();
        session.dispose();
        LinkedList<HistoryEvent> events = new LinkedList<>();
        events.addAll(historyContainer.getListHistoryEvent());
        PlatformKnowledgeBaseDisposeSessionEvent platformKnowledgeBaseDisposeSessionEvent = new PlatformKnowledgeBaseDisposeSessionEvent(-1, new Date(), this.ruleBaseID, events);
        //TODO optimize
        SendToJMSThread send = new SendToJMSThread(this, platformKnowledgeBaseDisposeSessionEvent);
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

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public void fireEvent(HistoryEvent newHistoryEvent) {
        PlatformManagementKnowledgeBean bean = new PlatformManagementKnowledgeBean();
        bean.setHistoryEvent(newHistoryEvent);
        bean.setRequestRuntimePlarform(RequestRuntimePlarform.historyEvent);
        boolean transmitted = false;
        try {
            for (HistoryEvent toTransmitBefore : notYetTransmittedEvents) {
                this.webSocketClient.sendMessage(createBeanForHistoryEvent(toTransmitBefore));
            }
            this.webSocketClient.sendMessage(createBeanForHistoryEvent(newHistoryEvent));
            transmitted = true;
        } catch (IOException e) {
            logger.debug("DroolsPlatformKnowledgeBase", e);
        } catch (EncodeException e) {
            logger.debug("DroolsPlatformKnowledgeBase", e);
        } catch (DroolsChtijbugException e) {
            logger.debug("DroolsPlatformKnowledgeBase", e);
        }
        if (transmitted == false) {
            this.notYetTransmittedEvents.add(newHistoryEvent);
            this.webSocketClient.reconnectToServer();
        }
    }

    private PlatformManagementKnowledgeBean createBeanForHistoryEvent(HistoryEvent newHistoryEvent) {
        PlatformManagementKnowledgeBean bean = new PlatformManagementKnowledgeBean();
        bean.setHistoryEvent(newHistoryEvent);
        bean.setRequestRuntimePlarform(RequestRuntimePlarform.historyEvent);
        return bean;

    }
}
