package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.backend.wsclient.endpoint.DefaultSocketMessageListenerImpl;
import org.chtijbug.drools.platform.backend.wsclient.endpoint.WebSocketMessageListener;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Date;
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

    private Map<Integer, WebSocketClient> webSocketClientList = new HashMap<>();
    private Map<Integer, WebSocketMessageListener> webSocketClientMessageListenerList = new HashMap<>();
    private Map<Integer, Heartbeat> webSocketClientHeartBeatList = new HashMap<>();

    public WebSocketClient AddClient(PlatformRuntimeInstance platformRuntimeInstance) throws DeploymentException, IOException {

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setLastAlive(new Date());
        WebSocketMessageListener webSocketMessageListener = new DefaultSocketMessageListenerImpl(platformRuntimeInstance,heartbeat);
        WebSocketClient webSocketClient = new WebSocketClient(platformRuntimeInstance,webSocketMessageListener);
        this.webSocketClientList.put(platformRuntimeInstance.getRuleBaseID(), webSocketClient);
        this.webSocketClientMessageListenerList.put(platformRuntimeInstance.getRuleBaseID(), webSocketMessageListener);
        this.webSocketClientHeartBeatList.put(platformRuntimeInstance.getRuleBaseID(),heartbeat);

        return webSocketClient;

    }

    public void removeClient(PlatformRuntimeInstance platformRuntimeInstance) throws IOException {
        WebSocketClient webSocketClient = this.webSocketClientList.get(platformRuntimeInstance.getRuleBaseID());
        if (webSocketClient != null){
            webSocketClient.closeSession();
        }
        this.webSocketClientList.remove(platformRuntimeInstance.getRuleBaseID());
        this.webSocketClientMessageListenerList.remove(platformRuntimeInstance.getRuleBaseID());
        this.webSocketClientHeartBeatList.remove(platformRuntimeInstance.getRuleBaseID());
    }

    public Boolean exists(Integer ruleBaseID) {
        boolean result = false;
        if (webSocketClientList.containsKey(ruleBaseID)) {
            result = true;
        }

        return result;

    }

    public Boolean isAlive(Integer ruleBaseID) {
        boolean result = false;
        Heartbeat heartbeat = this.webSocketClientHeartBeatList.get(ruleBaseID);
        Date dateLastHearBeat  = heartbeat.getLastAlive();
        Date currentDate = new Date();
        long diff = currentDate.getTime() - dateLastHearBeat.getTime();
        if (diff< 60000) {
            result=true;
        }
        return result;
    }

    public WebSocketClient getWebSocketClient(Integer ruleBaseID) {
        WebSocketClient webSocketClient = null;
        if (webSocketClientList.containsKey(ruleBaseID)) {
            webSocketClient = webSocketClientList.get(ruleBaseID);
        }
        return webSocketClient;
    }
}
