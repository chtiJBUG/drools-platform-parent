package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.pojo.PlatformRuntime;
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
public class WebSocketClient {

    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);

    private PlatformRuntime platformRuntime = new PlatformRuntime();
    private Session session;


    public WebSocketClient(String hostname, int port, String endPoint) throws DeploymentException, IOException {
        this.platformRuntime.setHostname(hostname);
        this.platformRuntime.setPort(port);
        this.platformRuntime.setEndPoint(endPoint);
        ClientManager client = ClientManager.createClient();
        WebSocketClientBean beanClient = new WebSocketClientBean();

        this.session = client.connectToServer(
                beanClient,
                ClientEndpointConfig.Builder.create()
                        .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                        .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                        .build(),
                URI.create("ws://" + hostname + ":" + port + endPoint));

    }

    public PlatformRuntime getPlatformRuntime() {
        return platformRuntime;
    }

    public Session getSession() {
        return session;
    }
}
