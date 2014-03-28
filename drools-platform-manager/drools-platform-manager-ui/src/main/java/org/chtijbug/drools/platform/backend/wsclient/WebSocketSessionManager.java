package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.stereotype.Component;

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
@Component
public class WebSocketSessionManager {

    private static final Logger LOG = Logger.getLogger(WebSocketSessionManager.class);

    private List<WebSocketClient> webSocketClientList = new ArrayList<WebSocketClient>();


    public void AddClient(PlatformRuntime  platformRuntime) throws DeploymentException, IOException {

        WebSocketClient webSocketClient = new WebSocketClient(platformRuntime);
        this.webSocketClientList.add(webSocketClient);


    }
}