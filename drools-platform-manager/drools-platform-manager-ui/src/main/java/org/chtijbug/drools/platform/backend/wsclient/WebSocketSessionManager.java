package org.chtijbug.drools.platform.backend.wsclient;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    @Value(value = "${knowledge.numberRetriesConnectionToRuntime}")
    private int numberRetries;


    @Value(value = "${knowledge.timeToWaitBetweenTwoRetries}")
    private int timeToWaitBetweenTwoRetries;

    public WebSocketClient AddClient(PlatformRuntimeInstance platformRuntimeInstance) throws DeploymentException, IOException {

        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setLastAlive(new Date());
        WebSocketClient webSocketClient = new WebSocketClient(platformRuntimeInstance, numberRetries, timeToWaitBetweenTwoRetries);
        this.webSocketClientList.put(platformRuntimeInstance.getRuleBaseID(), webSocketClient);

        return webSocketClient;

    }

    public Set<Integer> getAllRuleBaseID() {
        return webSocketClientList.keySet();
    }

    public void removeClient(Integer ruleBaseID) throws IOException {
        WebSocketClient webSocketClient = this.webSocketClientList.get(ruleBaseID);
        if (webSocketClient != null) {
            webSocketClient.closeSession();
        }
        this.webSocketClientList.remove(ruleBaseID);
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
        if (this.webSocketClientList != null && this.webSocketClientList.get(ruleBaseID) != null) {
            Heartbeat heartbeat = this.webSocketClientList.get(ruleBaseID).getHeartbeat();
            Date dateLastHearBeat = heartbeat.getLastAlive();
            if (heartbeat != null && heartbeat.getLastAlive() != null) {
                Date currentDate = new Date();
                long diff = currentDate.getTime() - dateLastHearBeat.getTime();
                if (diff < 60000) {
                    result = true;
                }
            }
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
