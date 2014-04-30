package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */

public class WebSocketServer extends Thread {


    private String ws_hostname;

    private int ws_port;

    public static HashMap<String, Object> userProperties = new HashMap<String, Object>();

    Server localWebSocketServer;

    private static final Logger LOG = Logger.getLogger(WebSocketServer.class);

    public DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;

    private Semaphore releaseWhenReady;
    private Semaphore tellWhenToStop;


    public WebSocketServer(String ws_hostname, int ws_port, DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase, Semaphore releaseWhenReady, Semaphore tellWhenToStop) throws UnknownHostException {
        this.ws_hostname = ws_hostname;
        this.ws_port = ws_port;
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;
        userProperties.put("droolsPlatformKnowledgeBase", droolsPlatformKnowledgeBase);
        this.releaseWhenReady = releaseWhenReady;
        this.tellWhenToStop = tellWhenToStop;
    }

    //@Override
    public void run() {


        this.localWebSocketServer = new Server(ws_hostname, ws_port, "/", userProperties, RuntimeWebSocketServerService.class);
        try {
            localWebSocketServer.start();
            this.releaseWhenReady.release();
            this.tellWhenToStop.acquire();
        } catch (DeploymentException e) {
            LOG.error("WebSocketServer.run", e);
        } catch (InterruptedException e) {
            LOG.error("WebSocketServer.run", e);

        }

    }

    public void end() {
        this.localWebSocketServer.stop();
        this.localWebSocketServer = null;
        this.tellWhenToStop.release();
    }


    public String getWs_hostname() {
        return ws_hostname;
    }

    public int getWs_port() {
        return ws_port;
    }


    public void setDroolsPlatformKnowledgeBase(DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase) {
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;
    }
}
