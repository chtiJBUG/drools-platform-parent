package org.chtijbug.drools.platform.runtime.servlet.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.PlatformManagementKnowledgeBeanServiceFactory;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.websocket.EncodeException;
import java.io.IOException;

public class SpringWebSocketServer extends AbstractWebSocketHandler implements WebSocketServerInstance {

    private static final Logger LOG = Logger.getLogger(SpringWebSocketServer.class);


    private WebSocketSession serverSession;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        this.serverSession = session;
    }


    @Override
    public void end() {

    }

    @Override
    public void run() {

    }

    @Override
    public void sendHeartBeat() {

        if (serverSession != null && serverSession.isOpen()) {
            PlatformManagementKnowledgeBean platformManagementKnowledgeBean = PlatformManagementKnowledgeBeanServiceFactory.generateHearBeatBean();

            WebSocketMessage<PlatformManagementKnowledgeBean> platformManagementKnowledgeBeanWebSocketMessage = new WebSocketMessage<PlatformManagementKnowledgeBean>() {
                @Override
                public PlatformManagementKnowledgeBean getPayload() {
                    return PlatformManagementKnowledgeBeanServiceFactory.generateHearBeatBean();
                }

                @Override
                public boolean isLast() {
                    return true;
                }
            };
            try {
                serverSession.sendMessage(platformManagementKnowledgeBeanWebSocketMessage);
            } catch (IOException e) {
                LOG.error("sendHeartBeat not possible", e);
            }
        }

    }

    @Override
    public void sendMessage(final PlatformManagementKnowledgeBean platformManagementKnowledgeBean) throws IOException, EncodeException {

        if (serverSession != null && serverSession.isOpen()) {
            try {
                WebSocketMessage<PlatformManagementKnowledgeBean> platformManagementKnowledgeBeanWebSocketMessage = new WebSocketMessage<PlatformManagementKnowledgeBean>() {
                    @Override
                    public PlatformManagementKnowledgeBean getPayload() {
                        return platformManagementKnowledgeBean;
                    }

                    @Override
                    public boolean isLast() {
                        return true;
                    }
                };
                serverSession.sendMessage(platformManagementKnowledgeBeanWebSocketMessage);
            } catch (IOException e) {
                LOG.error("sendHeartBeat not possible", e);
            }
        }

    }

    @Override
    public String getHostName() {
        return null;
    }

    @Override
    public int getPort() {
        return 0;
    }
}