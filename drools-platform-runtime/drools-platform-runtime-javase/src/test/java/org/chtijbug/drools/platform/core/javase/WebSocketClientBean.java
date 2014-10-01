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

import javax.websocket.*;
import java.io.IOException;


@ClientEndpoint(encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class WebSocketClientBean
        extends Endpoint {
    private static final Logger LOG = Logger.getLogger(WebSocketClient.class);


    private Session peerLoggerClient;
    public static WebSocketClientListenerInterface webSocketClientListenerInterface;

    public WebSocketClientBean() {
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
        this.peerLoggerClient.getBasicRemote().sendObject(bean);
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.peerLoggerClient = session;
        session.addMessageHandler(new MessageHandler.Whole<PlatformManagementKnowledgeBean>() {
            @Override
            public void onMessage(PlatformManagementKnowledgeBean bean) {
                if (webSocketClientListenerInterface != null) {
                    webSocketClientListenerInterface.answer(bean);

                }

            }
        });
    }
}
