package org.chtijbug.drools.platform.backend.service.heartbeat;

import org.chtijbug.drools.platform.backend.wsclient.WebSocketClient;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
@Component
public class HeartbeatManagementService {

    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    @Scheduled(cron = "0/20 * * * * *")
    public void SynchronizeGuvnorCategories() {


        for (Integer ruleBaseID : webSocketSessionManager.getAllRuleBaseID()) {
            WebSocketClient webSocketClient = webSocketSessionManager.getWebSocketClient(ruleBaseID);
            PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
            platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.isAlive);
            try {
                webSocketClient.sendMessage(platformManagementKnowledgeBean);
            } catch (EncodeException e) {
                //Do NOTHING
            } catch (IOException e) {
                //DO Nothing
            }

        }


    }
}