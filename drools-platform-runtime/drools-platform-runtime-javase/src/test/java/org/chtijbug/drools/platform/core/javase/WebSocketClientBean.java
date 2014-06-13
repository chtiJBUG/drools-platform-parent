package org.chtijbug.drools.platform.core.javase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 21/01/14
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */

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
