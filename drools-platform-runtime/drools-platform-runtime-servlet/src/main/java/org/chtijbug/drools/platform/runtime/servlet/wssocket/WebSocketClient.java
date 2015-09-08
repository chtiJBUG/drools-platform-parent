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
package org.chtijbug.drools.platform.runtime.servlet.wssocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.PlatformManagementKnowledgeBeanServiceFactory;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.runtime.servlet.AppContext;
import org.chtijbug.drools.platform.runtime.servlet.DroolsPlatformKnowledgeBaseJavaEE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.glassfish.tyrus.client.ClientManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClient
        extends Endpoint {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);
    final private Heartbeat heartbeat = new Heartbeat();
    @Value(value = "${knowledge.timeToWaitBetweenTwoRetries}")
    private final int timeToWaitBetweenTwoRetries;
    String hostname;
    Integer portNumber;
    String endPointName;
    private int numberRetries;
    private DroolsPlatformKnowledgeBase platformKnowledgeBaseJavaEE;
    private Session session;


    public WebSocketClient(String hostname, Integer portNumber, String endPointName, int numberRetries, int timeToWaitBetweenTwoRetries) throws DeploymentException, IOException {
        this.platformKnowledgeBaseJavaEE = AppContext.getApplicationContext().getBean(DroolsPlatformKnowledgeBase.class);
        this.numberRetries = numberRetries;
        this.timeToWaitBetweenTwoRetries = timeToWaitBetweenTwoRetries;

        /**
         * Let us start n times teh connection + wait between timeout
         */
        boolean connected = false;
        int retryNumber = 0;
        Exception lastException = null;
        while (retryNumber < this.numberRetries && connected == false) {
            try {
                ClientManager client = ClientManager.createClient();
                this.session = client.connectToServer(
                        this,
                        ClientEndpointConfig.Builder.create()
                                .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                                .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                                .build(),

                        URI.create("ws://" + hostname + ":" + portNumber + endPointName)
                );
                connected = true;
                LOG.info("Successfully Connected to" + "ws://" + hostname + ":" + portNumber + endPointName);
            } catch (Exception e) {
                lastException = e;
                try {
                    Thread.sleep(this.timeToWaitBetweenTwoRetries);
                } catch (InterruptedException e1) {
                    LOG.error("Could not  wait  " + "ws://" + hostname + ":" + portNumber + endPointName + " Try number=" + retryNumber, e1);
                }
            } finally {
                retryNumber++;
            }
        }
        if (connected == false && retryNumber >= this.numberRetries) {
            if (lastException != null) {
                LOG.error("Could not Connect to " + "ws://" + hostname + ":" + portNumber + endPointName + " after =" + retryNumber, lastException);

                if (lastException instanceof IOException) {
                    throw (IOException) lastException;
                }
            }
        }
    }


    @Override
    public void onClose(Session session, CloseReason closeReason) {

        super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {

        super.onError(session, thr);
    }

    public void sendMessage(PlatformManagementKnowledgeBean bean) throws IOException, EncodeException {
        this.session.getBasicRemote().sendObject(bean);
    }


    @Override
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        this.session = session;
        final DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE2 = this.platformKnowledgeBaseJavaEE;
        final WebSocketClient webclient = this;
        session.addMessageHandler(new MessageHandler.Whole<PlatformManagementKnowledgeBean>() {
            @Override
            public void onMessage(PlatformManagementKnowledgeBean bean) {

                switch (bean.getRequestRuntimePlarform()) {
                    case isAlive:

                        try {
                            webclient.sendMessage(PlatformManagementKnowledgeBeanServiceFactory.isAlive(bean));
                        } catch (IOException e) {
                            LOG.debug("WebSocketCLientOnMessage.isAlice", e);
                        } catch (EncodeException e) {
                            LOG.debug("WebSocketCLientOnMessage.isAlice", e);
                        }

                        break;
                    case duplicateRuleBaseID:
                        platformKnowledgeBaseJavaEE2.dispose();
                        LOG.error("duplicated ruleBaseID " + bean.toString());
                        break;
                    case ruleVersionInfos:
                        bean = PlatformManagementKnowledgeBeanServiceFactory.generateRuleVersionsInfo(bean, platformKnowledgeBaseJavaEE.getDroolsResources());
                        try {
                            webclient.sendMessage(bean);
                        } catch (IOException e) {
                            LOG.debug("WebSocketCLientOnMessage.ruleVersionsInfos", e);
                        } catch (EncodeException e) {
                            LOG.debug("WebSocketCLientOnMessage.ruleVersionsInfos", e);
                        }
                        break;
                    case loadNewRuleVersion:
                        List<DroolsResource> droolsResources = PlatformManagementKnowledgeBeanServiceFactory.extract(bean.getResourceFileList(), platformKnowledgeBaseJavaEE.getGuvnorUsername(), platformKnowledgeBaseJavaEE.getGuvnorPassword());
                        try {
                            platformKnowledgeBaseJavaEE.RecreateKBaseWithNewRessources(droolsResources);
                            bean.setRequestStatus(RequestStatus.SUCCESS);
                            webclient.sendMessage(bean);
                            platformKnowledgeBaseJavaEE2.setRuleBaseStatus(true);
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
                            try {
                                webclient.sendMessage(bean);
                            } catch (IOException e1) {
                                LOG.debug("WebSocketCLientOnMessage.loadnewRuleVersion", e);
                            } catch (EncodeException e1) {
                                LOG.debug("WebSocketCLientOnMessage.loadnewRuleVersion", e);
                            }
                        }
                        break;
                }
            }
        });


    }


    public Heartbeat getHeartbeat() {
        return heartbeat;
    }

    public Session getSession() {
        return this.session;
    }

    public void closeSession() throws IOException {
        if (this.session != null) {
            if (this.session.isOpen() == true) {
                if (this.session.getMessageHandlers().size() > 0) {
                    for (MessageHandler messageHandler : this.session.getMessageHandlers()) {
                        this.session.removeMessageHandler(messageHandler);
                    }
                }
                this.session.close();
            }


        }
    }

}
