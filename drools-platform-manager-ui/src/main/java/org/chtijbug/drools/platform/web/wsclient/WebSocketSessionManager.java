package org.chtijbug.drools.platform.web.wsclient;

import org.apache.log4j.Logger;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:37
 * To change this template use File | Settings | File Templates.
 */
public class WebSocketSessionManager {

    private static final Logger LOG = Logger.getLogger(WebSocketSessionManager.class);

    private List<WebSocketClient> webSocketClientList = new ArrayList<WebSocketClient>();


    public void AddClient(String hostName, int port, String endPoint) throws DeploymentException, IOException {

        WebSocketClient webSocketClient = new WebSocketClient(hostName, port, endPoint);
        this.webSocketClientList.add(webSocketClient);


    }
}
