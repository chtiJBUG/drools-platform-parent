package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsResource;
import org.chtijbug.drools.runtime.resource.DrlDroolsResource;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/runtime",
        encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class RuntimeWebSocketServerService {
    private static final Logger LOG = Logger.getLogger(RuntimeWebSocketServerService.class);

    private Session peerLoggerServer;
    private DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;
    private String guvnorUsername;
    private String guvnorPassword;

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
            case duplicateRuleBaseID:
                this.droolsPlatformKnowledgeBase.dispose();
                break;
            case ruleVersionInfos:
                for (DroolsResource droolsResource : droolsPlatformKnowledgeBase.getDroolsResources()) {
                    if (droolsResource instanceof GuvnorDroolsResource) {
                        GuvnorDroolsResource guvnorDroolsResource = (GuvnorDroolsResource) droolsResource;
                        PlatformResourceFile platformResourceFile = new PlatformResourceFile(guvnorDroolsResource.getBaseUrl(), guvnorDroolsResource.getWebappName(), guvnorDroolsResource.getPackageName(), guvnorDroolsResource.getPackageVersion(), null, null);
                        bean.getResourceFileList().add(platformResourceFile);
                    } else if (droolsResource instanceof DrlDroolsResource) {
                        DrlDroolsResource drlDroolsResource = (DrlDroolsResource) droolsResource;
                        PlatformResourceFile platformResourceFile = new PlatformResourceFile(drlDroolsResource.getFileName(), drlDroolsResource.getFileContent());
                        bean.getResourceFileList().add(platformResourceFile);
                    }
                }
                bean.setRequestStatus(RequestStatus.SUCCESS);
                LOG.info("Server Side before sent" + bean.toString());
                peer.getBasicRemote().sendObject(bean);
                LOG.info("Server Side after sent");

                break;
            case loadNewRuleVersion:
                List<PlatformResourceFile> resourceFiles = bean.getResourceFileList();
                List<DroolsResource> droolsResources = new ArrayList<>();
                for (PlatformResourceFile resourceFile : resourceFiles) {
                    if (resourceFile.getGuvnor_url() != null) {
                        DroolsResource droolsResource = GuvnorDroolsResource.createGuvnorRessource(resourceFile.getGuvnor_url(), resourceFile.getGuvnor_appName(), resourceFile.getGuvnor_packageName(), resourceFile.getGuvnor_packageVersion(), guvnorUsername, guvnorPassword);
                        droolsResources.add(droolsResource);
                    } else {
                        String extension = getFileExtension(resourceFile.getFileName());
                        if ("bpmn2".equals(extension)) {
                            Bpmn2DroolsResource bpmn2DroolsResource = Bpmn2DroolsResource.createClassPathResource(resourceFile.getFileName());
                            droolsResources.add(bpmn2DroolsResource);
                        } else if ("drl".equals(extension)) {
                            DroolsResource droolsResource = DrlDroolsResource.createClassPathResource(resourceFile.getFileName());
                            droolsResources.add(droolsResource);
                        }
                    }
                }
                try {
                    droolsPlatformKnowledgeBase.RecreateKBaseWithNewRessources(droolsResources);
                    bean.setRequestStatus(RequestStatus.SUCCESS);
                    peer.getBasicRemote().sendObject(bean);
                    this.droolsPlatformKnowledgeBase.setRuleBaseStatus(true);
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

    public void sendHeartBeat() {
        if (this.peerLoggerServer != null) {
            PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
        }
    }

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        Map<String, Object> userProperties = WebSocketServer.userProperties;
        this.droolsPlatformKnowledgeBase = (DroolsPlatformKnowledgeBase) userProperties.get("droolsPlatformKnowledgeBase");
        this.peerLoggerServer = session;
        this.droolsPlatformKnowledgeBase.setRuntimeWebSocketServerService(this);
        this.guvnorUsername = this.droolsPlatformKnowledgeBase.getGuvnorUsername();
        this.guvnorPassword = this.droolsPlatformKnowledgeBase.getGuvnorPassword();
        LOG.info("Server connected " + session + " " + endpointConfig);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        //prepare the endpoint for closing.
    }

    private String getFileExtension(String fileName) {
        String extension = null;
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}  