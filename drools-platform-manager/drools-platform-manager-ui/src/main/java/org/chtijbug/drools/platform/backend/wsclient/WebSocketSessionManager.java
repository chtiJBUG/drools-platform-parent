/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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


@Component
public class WebSocketSessionManager {

    private static final Logger LOG = Logger.getLogger(WebSocketSessionManager.class);

    private Map<Long, WebSocketClient> webSocketClientList = new HashMap<>();

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

    public Set<Long> getAllRuleBaseID() {
        return webSocketClientList.keySet();
    }

    public void removeClient(Long ruleBaseID) throws IOException {
        WebSocketClient webSocketClient = this.webSocketClientList.get(ruleBaseID);
        if (webSocketClient != null) {
            webSocketClient.closeSession();
        }
        this.webSocketClientList.remove(ruleBaseID);
    }

    public Boolean exists(Long ruleBaseID) {
        boolean result = false;
        if (webSocketClientList.containsKey(ruleBaseID)) {
            result = true;
        }

        return result;

    }


    public Boolean isAlive(Long ruleBaseID) {
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

    public WebSocketClient getWebSocketClient(Long ruleBaseID) {
        WebSocketClient webSocketClient = null;
        if (webSocketClientList.containsKey(ruleBaseID)) {
            webSocketClient = webSocketClientList.get(ruleBaseID);
        }
        return webSocketClient;
    }
}
