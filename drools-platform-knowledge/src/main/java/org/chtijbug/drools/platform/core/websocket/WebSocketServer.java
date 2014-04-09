package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
@Component
public class WebSocketServer {


    @Value("${ws.hostname}")
    private String ws_hostname;
    @Value("${ws.port}")
    private int ws_port;

    public static HashMap<String, Object> userProperties = new HashMap<String, Object>();

    Server localWebSocketServer;

    private static final Logger LOG = Logger.getLogger(WebSocketServer.class);

    public DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;

    public WebSocketServer() throws UnknownHostException {
        run();
    }

    public void run() throws UnknownHostException {


        this.localWebSocketServer = new Server(ws_hostname, ws_port, "/", userProperties, RuntimeWebSocketServerService.class);
        try {
            localWebSocketServer.start();
        } catch (DeploymentException e) {
            LOG.error("WebSocketServer.run", e);
        }

    }
    public  void stop() {
        this.localWebSocketServer.stop();
        this.localWebSocketServer=null;
    }


    public String getWs_hostname() {
        return ws_hostname;
    }

    public int getWs_port() {
        return ws_port;
    }


    public void setDroolsPlatformKnowledgeBase(DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase) {
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;
        userProperties.put("droolsPlatformKnowledgeBase",droolsPlatformKnowledgeBase);
    }
}
