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
package org.chtijbug.drools.platform.core.wssocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.PlatformManagementKnowledgeBeanServiceFactory;
import org.chtijbug.drools.platform.core.ReconnectToServerThread;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.auth.AuthConfig;
import org.glassfish.tyrus.client.auth.AuthenticationException;
import org.glassfish.tyrus.client.auth.Authenticator;
import org.glassfish.tyrus.client.auth.Credentials;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClient
        extends Endpoint implements MessageHandler.Whole<PlatformManagementKnowledgeBean> {
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);
    final private Heartbeat heartbeat = new Heartbeat();
    private final int timeToWaitBetweenTwoRetries;
    String hostname;
    Integer portNumber;
    String endPointName;
    private int numberRetries;
    private DroolsPlatformKnowledgeBase platformKnowledgeBase;
    private Session session;
    private ClientManager client;
    private ReconnectToServerThread reconnectToServerThread;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public WebSocketClient(String hostname, Integer portNumber, String endPointName, int numberRetries, int timeToWaitBetweenTwoRetries, DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase) throws DeploymentException, IOException {
        this.platformKnowledgeBase = droolsPlatformKnowledgeBase;
        this.numberRetries = numberRetries;
        this.timeToWaitBetweenTwoRetries = timeToWaitBetweenTwoRetries;
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.endPointName = endPointName;

        /**
         * Let us start n times teh connection + wait between timeout
         */
        this.connectToServer();
    }

    public void connectToServer() throws IOException {
        /**
         * Let us start n times teh connection + wait between timeout
         */
        boolean connected = false;
        int retryNumber = 0;
        Exception lastException = null;
        while (connected == false) {
            try {
                /**
                Authenticator authenticator = new Authenticator() {
                    @Override
                    public String generateAuthorizationHeader(URI uri, String s, Credentials credentials) throws AuthenticationException {
                        return null;
                    }
                };
                AuthConfig authConfig = AuthConfig.Builder.create().build();
                 **/
                client = ClientManager.createClient();
                // client.getProperties().put(ClientProperties.AUTH_CONFIG, authConfig);
                client.getProperties().put(ClientProperties.CREDENTIALS, new Credentials("admin", "admin"));
                this.session = client.connectToServer(
                        this,
                        ClientEndpointConfig.Builder.create()
                                .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                                .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                                .build(),

                        URI.create("ws://" + hostname + ":" + portNumber + endPointName)
                );
                connected = true;
                LOG.info("Successfully Connected to " + "ws://" + hostname + ":" + portNumber + endPointName);
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
        logger.debug("WebSocketClient.onClose", closeReason);
        // super.onClose(session, closeReason);
    }

    @Override
    public void onError(Session session, Throwable thr) {

        logger.error("WebSocketClient.onClose", thr);
    }

    public void sendMessage(PlatformManagementKnowledgeBean bean) throws IOException, EncodeException, DroolsChtijbugException {
        try {
            if (this.session != null && this.session.isOpen()) {
                if (bean != null && bean.getHistoryEvent() != null) {

                    this.session.getBasicRemote().sendBinary(this.getBuffer(bean));
                } else {

                    this.session.getBasicRemote().sendObject(bean);
                }
            } else {
                DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("WebSocketClient", "noConnectionToServer", null);
                throw droolsChtijbugException;
            }
        } catch (Exception e) {
            logger.error("WebSocketClient.sendMessage", e);
            throw e;
        }


    }


    @Override
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        //      this.session = session;
        final DroolsPlatformKnowledgeBase platformKnowledgeBaseJavaEE2 = this.platformKnowledgeBase;
        final WebSocketClient webclient = this;
        session.addMessageHandler(this);
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

    public ByteBuffer getBuffer(PlatformManagementKnowledgeBean bean) {


        try {
            byte[] bytes = ByteUtil.toByteArray(bean);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onMessage(PlatformManagementKnowledgeBean bean) {
        switch (bean.getRequestRuntimePlarform()) {
            case isAlive:

                try {
                    this.sendMessage(PlatformManagementKnowledgeBeanServiceFactory.isAlive(bean));
                } catch (IOException e) {
                    LOG.debug("WebSocketCLientOnMessage.isAlice", e);
                } catch (EncodeException e) {
                    LOG.debug("WebSocketCLientOnMessage.isAlice", e);
                } catch (DroolsChtijbugException e) {
                    LOG.debug("WebSocketCLientOnMessage.isAlice", e);
                }

                break;
            case duplicateRuleBaseID:
                this.platformKnowledgeBase.dispose();
                LOG.error("duplicated ruleBaseID " + bean.toString());
                break;
            case ruleVersionInfos:
                bean = PlatformManagementKnowledgeBeanServiceFactory.generateRuleVersionsInfo(bean, platformKnowledgeBase.getDroolsResources());
                try {
                    this.sendMessage(bean);
                } catch (IOException e) {
                    LOG.debug("WebSocketCLientOnMessage.ruleVersionsInfos", e);
                } catch (EncodeException e) {
                    LOG.debug("WebSocketCLientOnMessage.ruleVersionsInfos", e);
                } catch (DroolsChtijbugException e) {
                    LOG.debug("WebSocketCLientOnMessage.ruleVersionsInfos", e);
                }
                break;
            case loadNewRuleVersion:
                List<DroolsResource> droolsResources = PlatformManagementKnowledgeBeanServiceFactory.extract(bean.getResourceFileList(), platformKnowledgeBase.getGuvnorUsername(), platformKnowledgeBase.getGuvnorPassword());
                try {
                    platformKnowledgeBase.RecreateKBaseWithNewRessources(droolsResources);
                    bean.setRequestStatus(RequestStatus.SUCCESS);
                    this.sendMessage(bean);
                    platformKnowledgeBase.setRuleBaseStatus(true);
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
                        this.sendMessage(bean);
                    } catch (IOException e1) {
                        LOG.debug("WebSocketCLientOnMessage.loadnewRuleVersion", e);
                    } catch (EncodeException e1) {
                        LOG.debug("WebSocketCLientOnMessage.loadnewRuleVersion", e);
                    } catch (DroolsChtijbugException e1) {
                        LOG.debug("WebSocketCLientOnMessage.loadnewRuleVersion", e);
                    }
                }
                break;
        }
    }

    public void reconnectToServer() {
        reconnectToServerThread = new ReconnectToServerThread(this);
        executorService.submit(reconnectToServerThread);


    }
}
