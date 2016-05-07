package org.chtijbug.drools.platform.rules;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.WorkbenchKnowledgeResource;
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
                PlatformResourceFile platformResourceFile = new PlatformResourceFile();
                bean.getResourceFileList().add(platformResourceFile);


                bean.setRequestStatus(RequestStatus.SUCCESS);
                peer.getBasicRemote().sendObject(bean);
                LOG.info("Runtime Guvnor Version " + platformResourceFile);
            case loadNewRuleVersion:
                PlatformResourceFile guvnorResourceFile2 = (PlatformResourceFile)bean.getResourceFileList().get(0);
                WorkbenchKnowledgeResource droolsResource = new WorkbenchKnowledgeResource(guvnorResourceFile2.getGuvnor_url(), guvnorResourceFile2.getArtifactId(), guvnorResourceFile2.getArtifactId(), guvnorResourceFile2.getVersion(), guvnor_username, guvnor_password);
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