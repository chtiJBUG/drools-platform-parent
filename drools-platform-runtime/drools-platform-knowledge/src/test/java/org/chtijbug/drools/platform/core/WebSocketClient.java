package org.chtijbug.drools.platform.core;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:05
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketClient extends Thread {

    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);

    private Session session;

    public WebSocketClient() throws DeploymentException, IOException, InterruptedException {

    }

    @Override
    public synchronized void start() {
        try {

            ClientManager client = ClientManager.createClient();
            WebSocketClientBean beanClient = new WebSocketClientBean();
            this.session = client.connectToServer(
                    beanClient,
                    ClientEndpointConfig.Builder.create()
                            .encoders(Arrays.<Class<? extends Encoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                            .decoders(Arrays.<Class<? extends Decoder>>asList(PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class))
                            .build(),
                    URI.create("ws://" + "localhost" + ":" + 8025 + "/runtime")
            );

        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Session getSession() {
        return session;
    }

    public void end() {

    }
}
