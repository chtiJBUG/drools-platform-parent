package org.chtijbug.drools.platform.runtime.javase.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.platform.core.PlatformManagementKnowledgeBeanServiceFactory;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.runtime.javase.DroolsPlatformKnowledgeBaseJavaSE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/runtime",
        encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class RuntimeWebSocketServerService {
    private static final Logger LOG = Logger.getLogger(RuntimeWebSocketServerService.class);

    private Session peerLoggerServer;
    private DroolsPlatformKnowledgeBaseJavaSE droolsPlatformKnowledgeBaseJavaSE;
    private String guvnorUsername;
    private String guvnorPassword;

    @OnMessage
    public void receiveMessage(PlatformManagementKnowledgeBean bean, Session peer) throws IOException, EncodeException {
        //
        switch (bean.getRequestRuntimePlarform()) {
            case isAlive:
                peer.getBasicRemote().sendObject(PlatformManagementKnowledgeBeanServiceFactory.isAlive(bean));
                LOG.info("Runtime is alive");
                break;
            case duplicateRuleBaseID:
                this.droolsPlatformKnowledgeBaseJavaSE.dispose();
                break;
            case ruleVersionInfos:
                bean = PlatformManagementKnowledgeBeanServiceFactory.generateRuleVersionsInfo(bean, droolsPlatformKnowledgeBaseJavaSE.getDroolsResources());
                LOG.info("Server Side before sent" + bean.toString());
                this.sendMessage(bean);
                LOG.info("Server Side after sent");

                break;
            case loadNewRuleVersion:
                ClassLoader classLoader = Thread.currentThread()
                        .getContextClassLoader();
                if (droolsPlatformKnowledgeBaseJavaSE.getProjectClassLoader() != null) {
                    Thread.currentThread().setContextClassLoader(droolsPlatformKnowledgeBaseJavaSE.getProjectClassLoader());
                }
                List<KnowledgeResource> droolsResources = PlatformManagementKnowledgeBeanServiceFactory.extract(bean.getResourceFileList(), guvnorUsername, guvnorPassword);
                try {
                    droolsPlatformKnowledgeBaseJavaSE.RecreateKBaseWithNewResources(droolsResources);
                    bean.setRequestStatus(RequestStatus.SUCCESS);
                    this.sendMessage(bean);
                    this.droolsPlatformKnowledgeBaseJavaSE.setRuleBaseStatus(true);
                } catch (Exception e) {
                    DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("RELOAD", "Could not reload Rule Package From Guvnor", e);
                    bean.setDroolsChtijbugException(droolsChtijbugException);
                    bean.setRequestStatus(RequestStatus.FAILURE);
                    this.sendMessage(bean);
                } finally {
                    if (droolsPlatformKnowledgeBaseJavaSE.getProjectClassLoader() != null && classLoader != null) {
                        Thread.currentThread().setContextClassLoader(classLoader);
                    }
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
        Map<String, Object> userProperties = WebSocketServer.userProperties;
        this.droolsPlatformKnowledgeBaseJavaSE = (DroolsPlatformKnowledgeBaseJavaSE) userProperties.get("droolsPlatformKnowledgeBase");
        userProperties.put("activeWebSocketService", this);
        this.peerLoggerServer = session;
        this.guvnorUsername = this.droolsPlatformKnowledgeBaseJavaSE.getGuvnorUsername();
        this.guvnorPassword = this.droolsPlatformKnowledgeBaseJavaSE.getGuvnorPassword();
        LOG.info("Server connected " + session + " " + endpointConfig);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        LOG.info("Server closed " + session + " " + reason);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        LOG.info("Server Error " + session + " " + t);
    }


}  