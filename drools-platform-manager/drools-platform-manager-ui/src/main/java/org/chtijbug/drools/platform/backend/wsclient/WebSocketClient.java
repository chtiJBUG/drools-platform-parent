package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClient
        extends Endpoint {

    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);

    private Session session;


    private WebSocketMessageListener webSocketMessageListener;

    public WebSocketClient(PlatformRuntimeInstance platformRuntimeInstance, WebSocketMessageListener webSocketMessageListener) throws DeploymentException, IOException {
        this.webSocketMessageListener = webSocketMessageListener;
        ClientManager client = ClientManager.createClient();
        this.session = client.connectToServer(
                this,
                ClientEndpointConfig.Builder.create()
                        .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                        .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                        .build(),
                URI.create("ws://" + platformRuntimeInstance.getHostname() + ":" + platformRuntimeInstance.getPort() + platformRuntimeInstance.getEndPoint())
        );
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
        session.addMessageHandler(new MessageHandler.Whole<PlatformManagementKnowledgeBean>() {
            @Override
            public void onMessage(PlatformManagementKnowledgeBean bean) {
                if (webSocketMessageListener != null) {
                    webSocketMessageListener.beanReceived(bean);
                }
            }
        });
    }


    public void closeSession() throws IOException {
        if (this.session != null) {
            if (this.session.getMessageHandlers().size() > 0) {
                for (MessageHandler messageHandler : this.session.getMessageHandlers()) {
                    this.session.removeMessageHandler(messageHandler);
                }
            }
            this.session.close();
        }
    }

}
