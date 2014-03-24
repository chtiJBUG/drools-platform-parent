package org.chtijbug.drools.platform.backend.wsclient;

import org.chtijbug.drools.platform.backend.service.AdministrationService;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import java.io.IOException;

import static java.lang.System.out;

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
    @Autowired
    private AdministrationService administrationService;

    private PlatformRuntime platformRuntime;

    private Session peerLoggerClient;

    public WebSocketClientBean(PlatformRuntime platformRuntime) {
        this.platformRuntime = platformRuntime;
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
                out.println("Message from server : " + bean.toString());
                switch (bean.getRequestRuntimePlarform()) {
                    case jmxInfos:

                        break;
                    case versionInfos:
                        break;
                    case isAlive:
                        break;
                    case ruleVersionInfos:
                        break;
                    case loadNewRuleVersion:
                        break;
                }

            }
        });
    }
}
