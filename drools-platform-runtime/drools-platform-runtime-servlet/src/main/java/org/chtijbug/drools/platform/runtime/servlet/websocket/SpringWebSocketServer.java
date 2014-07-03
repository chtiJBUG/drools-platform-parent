package org.chtijbug.drools.platform.runtime.servlet.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.PlatformManagementKnowledgeBeanServiceFactory;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.platform.runtime.servlet.DroolsPlatformKnowledgeBaseJavaEE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

public class SpringWebSocketServer extends TextWebSocketHandler implements WebSocketServerInstance {

    private static final Logger LOG = Logger.getLogger(SpringWebSocketServer.class);

    DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE;

    private WebSocketSession serverSession;

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        this.serverSession = session;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode stream = new PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode();

        PlatformManagementKnowledgeBean bean = stream.decode(new StringReader(message.getPayload()));
        switch (bean.getRequestRuntimePlarform()) {
            case isAlive:
                this.sendMessage(PlatformManagementKnowledgeBeanServiceFactory.isAlive(bean));
                LOG.info("Runtime is alive");
                break;
            case duplicateRuleBaseID:
                this.platformKnowledgeBaseJavaEE.dispose();
                break;
            case ruleVersionInfos:
                bean = PlatformManagementKnowledgeBeanServiceFactory.generateRuleVersionsInfo(bean, platformKnowledgeBaseJavaEE.getDroolsResources());
                LOG.info("Server Side before sent" + bean.toString());
                this.sendMessage(bean);
                LOG.info("Server Side after sent");

                break;
            case loadNewRuleVersion:
                List<DroolsResource> droolsResources = PlatformManagementKnowledgeBeanServiceFactory.extract(bean.getResourceFileList(), platformKnowledgeBaseJavaEE.getGuvnorUsername(), platformKnowledgeBaseJavaEE.getGuvnorPassword());
                try {
                    platformKnowledgeBaseJavaEE.RecreateKBaseWithNewRessources(droolsResources);
                    bean.setRequestStatus(RequestStatus.SUCCESS);
                    this.sendMessage(bean);
                    this.platformKnowledgeBaseJavaEE.setRuleBaseStatus(true);
                } catch (Exception e) {
                    DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("RELOAD", "Could not reload Rule Package From Guvnor", e);
                    bean.setDroolsChtijbugException(droolsChtijbugException);
                    bean.setRequestStatus(RequestStatus.FAILURE);
                    this.sendMessage(bean);
                }
                break;
        }


    }

    @Override
    public void end() {

    }

    @Override
    public void run() {

    }


    @Override
    public void sendMessage(final PlatformManagementKnowledgeBean platformManagementKnowledgeBean) throws DroolsChtijbugException {
        PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode stream = new PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode();
        if (serverSession != null && serverSession.isOpen()) {
            StringWriter writer = new StringWriter();
            try {
                stream.encode(platformManagementKnowledgeBean, writer);
                TextMessage response = new TextMessage(writer.toString());
                LOG.info(">> Server : " + response);
                serverSession.sendMessage(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EncodeException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public String getHostName() {
        return this.platformKnowledgeBaseJavaEE.getWebSocketHostname();
    }

    @Override
    public int getPort() {
        return this.platformKnowledgeBaseJavaEE.getWebSocketPort();
    }

    public void setDroolsPlatformKnowledgeBaseJavaEE(DroolsPlatformKnowledgeBaseJavaEE droolsPlatformKnowledgeBaseJavaEE) {
        this.platformKnowledgeBaseJavaEE = droolsPlatformKnowledgeBaseJavaEE;
    }
}