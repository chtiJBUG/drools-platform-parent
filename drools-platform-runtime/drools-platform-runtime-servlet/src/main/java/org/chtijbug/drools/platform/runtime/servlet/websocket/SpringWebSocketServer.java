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

package org.chtijbug.drools.platform.runtime.servlet.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.PlatformManagementKnowledgeBeanServiceFactory;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.runtime.servlet.DroolsPlatformKnowledgeBaseJavaEE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class SpringWebSocketServer extends TextWebSocketHandler implements WebSocketServerInstance {

    private static final Logger LOG = Logger.getLogger(SpringWebSocketServer.class);

    DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE;
    private WebSocketSession serverSession;

    private boolean initialConnection = false;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        this.serverSession = session;
        initialConnection = true;

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (initialConnection = true) {
            this.platformKnowledgeBaseJavaEE.sendPlatformKnowledgeBaseInitialConnectionEventToServer();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode stream = new PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode();

        PlatformManagementKnowledgeBean bean = stream.decode(new StringReader(message.getPayload()));
        switch (bean.getRequestRuntimePlarform()) {
            case isAlive:
                this.sendMessage(PlatformManagementKnowledgeBeanServiceFactory.isAlive(bean));
                break;
            case duplicateRuleBaseID:
                this.platformKnowledgeBaseJavaEE.dispose();
                LOG.error("duplicated ruleBaseID " + bean.toString());
                break;
            case ruleVersionInfos:
                bean = PlatformManagementKnowledgeBeanServiceFactory.generateRuleVersionsInfo(bean, platformKnowledgeBaseJavaEE.getDroolsResources());
                this.sendMessage(bean);
                break;
            case loadNewRuleVersion:
                List<DroolsResource> droolsResources = PlatformManagementKnowledgeBeanServiceFactory.extract(bean.getResourceFileList(), platformKnowledgeBaseJavaEE.getGuvnorUsername(), platformKnowledgeBaseJavaEE.getGuvnorPassword());
                try {
                    platformKnowledgeBaseJavaEE.RecreateKBaseWithNewRessources(droolsResources);
                    bean.setRequestStatus(RequestStatus.SUCCESS);
                    this.sendMessage(bean);
                    this.platformKnowledgeBaseJavaEE.setRuleBaseStatus(true);
                    StringBuilder ss = new StringBuilder();
                    for (DroolsResource dd : droolsResources) {
                        ss.append(dd.toString());
                    }
                    LOG.info("loadNewRuleVersion done" + ss.toString());
                } catch (Exception e) {
                    DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("RELOAD", "Could not reload Rule Package From Guvnor", e);
                    bean.setDroolsChtijbugException(droolsChtijbugException);
                    bean.setRequestStatus(RequestStatus.FAILURE);
                    LOG.error("Could not reload new rule version" + bean.toString(), e);
                    this.sendMessage(bean);
                }
                break;
        }


    }

    @Override
    public void end() {

    }

    @Override
    public void run() {

    }


    @Override
    public void sendMessage(final PlatformManagementKnowledgeBean platformManagementKnowledgeBean) throws DroolsChtijbugException {
        PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode stream = new PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode();
        if (serverSession != null && serverSession.isOpen()) {
            StringWriter writer = new StringWriter();
            try {
                stream.encode(platformManagementKnowledgeBean, writer);
                TextMessage response = new TextMessage(writer.toString());
                LOG.info(">> Server : " + response);
                serverSession.sendMessage(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String getHostName() {
        return this.platformKnowledgeBaseJavaEE.getWebSocketHostname();
    }

    @Override
    public int getPort() {
        return this.platformKnowledgeBaseJavaEE.getWebSocketPort();
    }

    @Override
    public String getEndPoint() {
        return this.platformKnowledgeBaseJavaEE.getWebSocketEndPoint();
    }

    public void setPlatformKnowledgeBaseJavaEE(DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE) {
        this.platformKnowledgeBaseJavaEE = platformKnowledgeBaseJavaEE;
    }

    public void initServer() {
        this.platformKnowledgeBaseJavaEE.setWebSocketServer(this);
        this.platformKnowledgeBaseJavaEE.startConnectionToPlatform();

    }
}