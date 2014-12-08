package org.chtijbug.drools.platform.runtime.javase.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.runtime.javase.DroolsPlatformKnowledgeBaseJavaSE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */

public class WebSocketServer implements WebSocketServerInstance {
    private static final Logger LOG = Logger.getLogger(WebSocketServer.class);
    protected static HashMap<String, Object> userProperties = new HashMap<>();
    private String ws_hostname;
    private int ws_port;
    private String ws_endpoint = "/runtime";
    Server localWebSocketServer;


    public DroolsPlatformKnowledgeBaseJavaSE droolsPlatformKnowledgeBase;

    public WebSocketServer(String ws_hostname, int ws_port, DroolsPlatformKnowledgeBaseJavaSE droolsPlatformKnowledgeBase) throws UnknownHostException {
        this.ws_hostname = ws_hostname;
        this.ws_port = ws_port;
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;
        userProperties.put("droolsPlatformKnowledgeBase", droolsPlatformKnowledgeBase);
    }

    public void run() {
        this.localWebSocketServer = new Server(ws_hostname, ws_port, "/", userProperties, RuntimeWebSocketServerService.class);
        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        ClassLoader currentClassloader = this.getClass().getClassLoader();

        if (classLoader != null) {
            this.droolsPlatformKnowledgeBase.setProjectClassLoader(classLoader);
            Thread.currentThread().setContextClassLoader(currentClassloader);
        }

        try {
            localWebSocketServer.start();
        } catch (DeploymentException e) {
            LOG.error("WebSocketServer.run", e);

        } finally {
            if (classLoader != null) {
                Thread.currentThread().setContextClassLoader(classLoader);
            }
        }
    }


    @Override
    public void sendMessage(PlatformManagementKnowledgeBean platformManagementKnowledgeBean) throws DroolsChtijbugException {
        RuntimeWebSocketServerService runtimeWebSocketServerService = (RuntimeWebSocketServerService) userProperties.get("activeWebSocketService");
        if (runtimeWebSocketServerService != null) {
            try {
                runtimeWebSocketServerService.sendMessage(platformManagementKnowledgeBean);
            } catch (IOException | EncodeException e) {
                DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("WebSocketServer.sendMessage", platformManagementKnowledgeBean.toString(), e);
                throw droolsChtijbugException;
            }
        }

    }

    @Override
    public String getHostName() {
        return ws_hostname;
    }

    @Override
    public int getPort() {
        return ws_port;
    }

    @Override
    public String getEndPoint() {
        return this.ws_endpoint;
    }

    public void end() {
        this.localWebSocketServer.stop();
        this.localWebSocketServer = null;
    }

    public String getWs_hostname() {
        return ws_hostname;
    }

    public int getWs_port() {
        return ws_port;
    }

}
