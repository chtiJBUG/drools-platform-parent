package org.chtijbug.drools.platform.rules;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.GuvnorVersion;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;
import org.springframework.beans.factory.annotation.Value;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

import static java.lang.System.out;

@ServerEndpoint(value = "/runtime",
        encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class TestRuntimeWebSocketServerService {

    private Session peerLoggerServer;

    private static final Logger LOG = Logger.getLogger(TestRuntimeWebSocketServerService.class);


    @Value("${guvnor.username}")
    private String guvnor_username;
    @Value("${guvnor.password}")
    private String guvnor_password;

    @OnMessage
    public void receiveMessage(PlatformManagementKnowledgeBean bean, Session peer) throws IOException, EncodeException {
        //  
        switch (bean.getRequestRuntimePlarform()) {
            case isAlive:
                bean.setAlive(true);
                bean.setRequestStatus(RequestStatus.SUCCESS);
                peer.getBasicRemote().sendObject(bean);
                LOG.info("Runtime is alive");
                break;
            case ruleVersionInfos:
                GuvnorVersion createNewGuvnorVersion = new GuvnorVersion();
                bean.setGuvnorVersion(createNewGuvnorVersion);

                bean.setRequestStatus(RequestStatus.SUCCESS);
                peer.getBasicRemote().sendObject(bean);
                LOG.info("Runtime Guvnor Version " + createNewGuvnorVersion);
                break;
            case loadNewRuleVersion:
                GuvnorVersion newGuvnorVersionToLoad = bean.getGuvnorVersion();
                DroolsResource droolsResource = new GuvnorDroolsResource(newGuvnorVersionToLoad.getGuvnor_url(), newGuvnorVersionToLoad.getGuvnor_appName(), newGuvnorVersionToLoad.getGuvnor_packageName(), newGuvnorVersionToLoad.getGuvnor_packageVersion(), guvnor_username, guvnor_password);
                try {

                    bean.setRequestStatus(RequestStatus.SUCCESS);
                    peer.getBasicRemote().sendObject(bean);
                } catch (Exception e) {
                    DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("RELOAD", "Could not reload Rule Package From Guvnor", e);
                    bean.setDroolsChtijbugException(droolsChtijbugException);
                    bean.setRequestStatus(RequestStatus.FAILURE);
                    peer.getBasicRemote().sendObject(bean);
               }
                break;
        }
        this.peerLoggerServer = peer;
        LOG.info("Websocket initialized with server logger");
    }

    public void sendMessage(PlatformManagementKnowledgeBean bean) throws IOException, EncodeException {
        this.peerLoggerServer.getBasicRemote().sendObject(bean);

    }

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        Map<String, Object> userProperties = endpointConfig.getUserProperties();
        this.peerLoggerServer = session;
         out.println("Server connected " + session + " " + endpointConfig);
    }
}  