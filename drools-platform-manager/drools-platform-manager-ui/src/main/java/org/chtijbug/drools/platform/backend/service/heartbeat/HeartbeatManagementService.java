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
package org.chtijbug.drools.platform.backend.service.heartbeat;

import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import java.io.IOException;


@Component
public class HeartbeatManagementService {

    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    //@Scheduled(cron = "0/20 * * * * *")
    public void SynchronizeGuvnorCategories() {


        for (Long ruleBaseID : webSocketSessionManager.getAllRuleBaseID()) {
            try {
                    PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
                    platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.isAlive);
                webSocketSessionManager.sendMessage(ruleBaseID, platformManagementKnowledgeBean);


            } catch (EncodeException e) {
                //Do NOTHING
            } catch (IOException e) {
                //DO Nothing
            }

        }


    }
}