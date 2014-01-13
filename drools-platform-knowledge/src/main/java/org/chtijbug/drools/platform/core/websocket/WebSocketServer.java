package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.glassfish.tyrus.server.Server;
import org.springframework.beans.factory.annotation.Value;

import javax.websocket.DeploymentException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */

public class WebSocketServer {


    @Value( "${ws.hostname}" )
       private String ws_hostname;
       @Value( "${ws.port}" )
       private int ws_port;
    private static final Logger LOG = Logger.getLogger(WebSocketServer.class);


    public void run() throws UnknownHostException {
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        String hostname = addr.getHostName();
        Server server = new Server(ws_hostname, ws_port, "/", RuntimeServerService.class);
        try {
            server.start();
        } catch (DeploymentException e) {
            LOG.error("WebSocketServer.run", e);
        }
    }
}
