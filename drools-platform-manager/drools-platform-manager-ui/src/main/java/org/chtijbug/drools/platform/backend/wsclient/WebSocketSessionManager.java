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
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class WebSocketSessionManager {

    private static final Logger LOG = Logger.getLogger(WebSocketSessionManager.class);

    private Map<Integer, WebSocketSession> webSocketClientList = new HashMap<>();


    public void AddClient(Integer ruleBaseID, WebSocketSession session) throws DeploymentException, IOException {

        this.webSocketClientList.put(ruleBaseID, session);


    }

    public void closeSession(WebSocketSession webSocketSessionClosed) {
        Set<Integer> listSessions = webSocketClientList.keySet();

        Integer ruleBaseIdToRemove = null;
        for (Integer ruleBaseId : listSessions) {
            WebSocketSession webSocketSession = webSocketClientList.get(ruleBaseId);
            if (webSocketSession != null && webSocketSession.equals(webSocketSessionClosed)) {
                ruleBaseIdToRemove = ruleBaseId;
            }
            break;
        }
        if (ruleBaseIdToRemove != null) {
            webSocketClientList.remove(ruleBaseIdToRemove);
        }
    }

    public Set<Integer> getAllRuleBaseID() {
        return webSocketClientList.keySet();
    }

    public void removeClient(Integer ruleBaseID) throws IOException {
        this.webSocketClientList.remove(ruleBaseID);
    }

    public Boolean exists(Integer ruleBaseID) {
        boolean result = false;
        if (webSocketClientList.containsKey(ruleBaseID)) {
            result = true;
        }

        return result;

    }

    public void sendMessage(Integer ruleBaseID, PlatformManagementKnowledgeBean bean) throws IOException, EncodeException {
        if (ruleBaseID != null) {
            WebSocketSession serverSession = this.webSocketClientList.get(ruleBaseID);
            PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode stream = new PlatformManagementKnowledgeBean.PlatformManagementKnowledgeBeanCode();
            if (serverSession != null && serverSession.isOpen()) {
                StringWriter writer = new StringWriter();
                try {
                    stream.encode(bean, writer);
                    TextMessage response = new TextMessage(writer.toString());
                    LOG.info(">> Server : " + response);
                    serverSession.sendMessage(response);
                } catch (IOException e) {
                    LOG.error("websocketSessionManager.sendMessage.IOException", e);
                } catch (EncodeException e) {
                    LOG.error("websocketSessionManager.sendMessage.EncodeException", e);
                }

            }
        }
    }


}
