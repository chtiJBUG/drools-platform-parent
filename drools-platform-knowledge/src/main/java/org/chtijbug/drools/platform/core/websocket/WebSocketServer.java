package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.net.UnknownHostException;

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
    @Value("${ws.port}")

    private static final Logger LOG = Logger.getLogger(WebSocketServer.class);

    public WebSocketServer() throws UnknownHostException {
        run();
    }

    public void run() throws UnknownHostException {
        Server server = new Server(ws_hostname, ws_port, "/", RuntimeWebSocketServerService.class);
        try {
            server.start();
        } catch (DeploymentException e) {
            LOG.error("WebSocketServer.run", e);
        }
    }

    public String getWs_hostname() {
        return ws_hostname;
    }

    public int getWs_port() {
        return ws_port;
    }
}
