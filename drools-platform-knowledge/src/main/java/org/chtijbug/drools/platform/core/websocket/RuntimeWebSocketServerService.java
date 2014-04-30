package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.ResourceFile;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.resource.DrlDroolsRessource;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@ServerEndpoint(value = "/runtime",
        encoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class},
        decoders = {PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode.class})
public class RuntimeWebSocketServerService {

    private Session peerLoggerServer;

    private static final Logger LOG = Logger.getLogger(RuntimeWebSocketServerService.class);

    private DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;



    private String guvnor_username;

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
                for (DroolsResource droolsResource : droolsPlatformKnowledgeBase.getDroolsResources()) {
                    if (droolsResource instanceof GuvnorDroolsResource) {
                        GuvnorDroolsResource guvnorDroolsResource = (GuvnorDroolsResource) droolsResource;
                        GuvnorResourceFile guvnorResourceFile = new GuvnorResourceFile();
                        guvnorResourceFile.setGuvnor_url(guvnorDroolsResource.getBaseUrl());
                        guvnorResourceFile.setGuvnor_appName(guvnorDroolsResource.getWebappName());
                        guvnorResourceFile.setGuvnor_packageName(guvnorDroolsResource.getPackageName());
                        guvnorResourceFile.setGuvnor_packageVersion(guvnorDroolsResource.getPackageVersion());
                        bean.getResourceFileList().add(guvnorResourceFile);
                    } else if (droolsResource instanceof DrlDroolsRessource) {
                        DrlDroolsRessource drlDroolsRessource = (DrlDroolsRessource) droolsResource;
                        DrlResourceFile drlResourceFile = new DrlResourceFile();
                        drlResourceFile.setFileName(drlDroolsRessource.getFileName());
                        drlResourceFile.setContent(drlDroolsRessource.getFileContent());
                        bean.getResourceFileList().add(drlResourceFile);
                    }
                }
                bean.setRequestStatus(RequestStatus.SUCCESS);
                System.out.println("Server Side before sent");
                peer.getBasicRemote().sendObject(bean);
                System.out.println("Server Side after sent");

                break;
            case loadNewRuleVersion:
                List<ResourceFile> resourceFiles = bean.getResourceFileList();
                List<DroolsResource> droolsResources = new ArrayList<>();
                for (ResourceFile resourceFile : resourceFiles) {
                    if (resourceFile instanceof GuvnorDroolsResource) {
                        GuvnorDroolsResource guvnorDroolsResource = (GuvnorDroolsResource) resourceFile;
                        DroolsResource droolsResource = new GuvnorDroolsResource(guvnorDroolsResource.getBaseUrl(), guvnorDroolsResource.getWebappName(), guvnorDroolsResource.getPackageName(), guvnorDroolsResource.getPackageVersion(), guvnor_username, guvnor_password);
                        droolsResources.add(droolsResource);
                    } else if (resourceFile instanceof DrlResourceFile) {
                        DrlResourceFile drlResourceFile = (DrlResourceFile) resourceFile;
                        DroolsResource droolsResource = DrlDroolsRessource.createClassPathResource(drlResourceFile.getFileName());
                        droolsResources.add(droolsResource);
                    }
                }
                try {
                    droolsPlatformKnowledgeBase.RecreateKBaseWithNewRessources(droolsResources);
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
        Map<String, Object> userProperties = WebSocketServer.userProperties;
        this.droolsPlatformKnowledgeBase = (DroolsPlatformKnowledgeBase) userProperties.get("droolsPlatformKnowledgeBase");
        this.peerLoggerServer = session;
        this.droolsPlatformKnowledgeBase.setRuntimeWebSocketServerService(this);
        out.println("Server connected " + session + " " + endpointConfig);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) throws IOException {
        //prepare the endpoint for closing.
    }
}  