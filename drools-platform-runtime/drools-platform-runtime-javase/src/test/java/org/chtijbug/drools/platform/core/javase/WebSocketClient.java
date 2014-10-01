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
package org.chtijbug.drools.platform.core.javase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;


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
