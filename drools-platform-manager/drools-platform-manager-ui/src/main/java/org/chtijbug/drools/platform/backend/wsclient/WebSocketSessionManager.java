package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 20:37
 * To change this template use File | Settings | File Templates.
 */
@Component
public class WebSocketSessionManager {

    private static final Logger LOG = Logger.getLogger(WebSocketSessionManager.class);

    private Map<Integer,WebSocketClient> webSocketClientList = new HashMap<>();


    public WebSocketClient AddClient(PlatformRuntime  platformRuntime) throws DeploymentException, IOException {

        WebSocketClient webSocketClient = new WebSocketClient(platformRuntime);
        this.webSocketClientList.put(platformRuntime.getRuleBaseID(),webSocketClient);
        return webSocketClient;

    }

    public WebSocketClient getWebSocketClient(Integer ruleBaseID){
        WebSocketClient webSocketClient = null;
        if (webSocketClientList.containsKey(ruleBaseID)){
             webSocketClient = webSocketClientList.get(ruleBaseID);
        }
        return webSocketClient;
    }
}
