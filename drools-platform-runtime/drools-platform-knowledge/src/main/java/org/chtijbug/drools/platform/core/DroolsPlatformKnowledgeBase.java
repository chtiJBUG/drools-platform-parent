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


import org.chtijbug.drools.entity.history.HistoryContainer;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.platform.core.callback.SpecificMessageCallback;
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

import org.chtijbug.drools.runtime.resource.FileKnowledgeResource;
import org.chtijbug.drools.runtime.resource.WorkbenchKnowledgeResource;
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
    private Long ruleBaseID;
    /**
     * Generic Message Callback
     */
    private SpecificMessageCallback specificMessageCallback;


    /**
     * Rule base singleton (Knwledge session factory)
     */
    private RuleBaseSingleton ruleBasePackage;
    private List<KnowledgeResource> droolsResources = new ArrayList<>();
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

    private String groupId;
    private String artifactId;
    private String version;
    private List<HistoryEvent> notYetTransmittedEvents = new LinkedList<>();

    public DroolsPlatformKnowledgeBase() {/* nop*/}

    public DroolsPlatformKnowledgeBase(Long ruleBaseID, String groupId,String artifactId, String version,List<KnowledgeResource> droolsResources, JavaDialect javaDialect) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.javaDialect = javaDialect;
        this.groupId=groupId;
        this.artifactId=artifactId;
        this.version=version;

    }

    public void setSpecificMessageCallback(SpecificMessageCallback specificMessageCallback) {
        this.specificMessageCallback = specificMessageCallback;
        if (this.webSocketClient!=null){
            this.webSocketClient.setSpecificMessageCallback(specificMessageCallback);
        }
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
            if (specificMessageCallback == null) {
                this.webSocketClient = new WebSocketClient(this.webSocketHostname, this.webSocketPort, this.webSocketEndPoint, 5, 2000, this);
            } else {
                this.webSocketClient = new WebSocketClient(this.webSocketHostname, this.webSocketPort, this.webSocketEndPoint, 5, 2000, this, specificMessageCallback);

            }
        } catch (DeploymentException e) {
            logger.debug("DroolsPlatformKnowledgeBaseJavaEE.initPlatformRuntime", e);
        } catch (IOException e) {
            logger.debug("DroolsPlatformKnowledgeBaseJavaEE.initPlatformRuntime", e);
        }
        ruleBasePackage = new RuleBaseSingleton(this.ruleBaseID,RuleBaseSingleton.DEFAULT_RULE_THRESHOLD,this.getHistoryListener(),this.groupId,this.artifactId,this.version );

        this.sendPlatformKnowledgeBaseInitialConnectionEventToServer();
        logger.debug("<<<initPlatformRuntime");
    }


    public void sendPlatformKnowledgeBaseInitialConnectionEventToServer() throws DroolsChtijbugException {
        PlatformKnowledgeBaseInitialConnectionEvent platformKnowledgeBaseInitialConnectionEvent = new PlatformKnowledgeBaseInitialConnectionEvent(-1L, new Date(), this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setRuleBaseID(this.ruleBaseID);
        platformKnowledgeBaseInitialConnectionEvent.setSessionId(-1L);
        platformKnowledgeBaseInitialConnectionEvent.setHostname("http://"+this.webSocketHostname);
        platformKnowledgeBaseInitialConnectionEvent.setPort(this.webSocketPort);
        platformKnowledgeBaseInitialConnectionEvent.setEndPoint(this.webSocketEndPoint);
        if (droolsResources.size() == 1 && droolsResources.get(0) instanceof WorkbenchKnowledgeResource) {
            WorkbenchKnowledgeResource guvnorDroolsResource = (WorkbenchKnowledgeResource) droolsResources.get(0);
             platformKnowledgeBaseInitialConnectionEvent.getKnowledgeResources().add(guvnorDroolsResource);
            this.guvnorUsername = guvnorDroolsResource.getUserName();
            this.guvnorPassword = guvnorDroolsResource.getPassword();
        } else {
            for (KnowledgeResource droolsResource : droolsResources) {
                platformKnowledgeBaseInitialConnectionEvent.getKnowledgeResources().add(droolsResource);
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
    public void loadKBase(String version) throws DroolsChtijbugException {
        this.ruleBasePackage.loadKBase(version);
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
    public HistoryListener getHistoryListener() {
        return this.ruleBasePackage.getHistoryListener();
    }

    @Override
    public Long getRuleBaseID() {
        return this.ruleBaseID;
    }

    public void setRuleBaseID(Long ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    @Override
    public void dispose() {
        this.ruleBasePackage.dispose();
        this.shutdown();
        this.executorService.shutdown();
    }



    public void cleanup() {

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
        PlatformKnowledgeBaseDisposeSessionEvent platformKnowledgeBaseDisposeSessionEvent = new PlatformKnowledgeBaseDisposeSessionEvent(-1L, new Date(), this.ruleBaseID, events);
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
    public List<KnowledgeResource> getDroolsResources() {
        return droolsResources;
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
