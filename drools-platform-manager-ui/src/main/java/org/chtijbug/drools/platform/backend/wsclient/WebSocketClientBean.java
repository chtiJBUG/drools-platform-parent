package org.chtijbug.drools.platform.backend.wsclient;

import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;

import javax.websocket.*;

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

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        session.addMessageHandler(new MessageHandler.Whole<PlatformManagementKnowledgeBean>() {

            @Override
            public void onMessage(PlatformManagementKnowledgeBean bean) {
                out.println("Message from server : " + bean.toString());
                switch (bean.getMessageRuntimePlatform()) {
                    case jmxInfos:
                        break;
                    case versionInfos:
                        break;
                }

            }
        });
    }
}
