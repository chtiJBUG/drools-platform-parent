package org.chtijbug.drools.platform.rules;

import org.apache.log4j.Logger;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/04/14
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TestWebSocketServer {

    @Value("${ws.hostname}")
    private String ws_hostname;
    @Value("${ws.port}")
    private int ws_port;

    Server localWebSocketServer;

    private static final Logger LOG = Logger.getLogger(TestWebSocketServer.class);

    public TestWebSocketServer() throws UnknownHostException {
        run();
    }

    public void run() throws UnknownHostException {
        HashMap<String, Object> userProperties = new HashMap<String, Object>();
        this.localWebSocketServer = new Server(ws_hostname, ws_port, "/", userProperties, TestRuntimeWebSocketServerService.class);
        try {
            localWebSocketServer.start();
        } catch (DeploymentException e) {
            LOG.error("WebSocketServer.run", e);
        }

    }

    public void stop() {
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

