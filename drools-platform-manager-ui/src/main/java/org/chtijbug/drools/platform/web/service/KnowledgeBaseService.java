package org.chtijbug.drools.platform.web.service;

import org.chtijbug.drools.platform.entity.PlatformRuntime;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
import org.chtijbug.drools.platform.web.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.persistence.RuntimeStorageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/01/14
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
@Component
public class KnowledgeBaseService {

    @Autowired
    WebSocketSessionManager webSocketSessionManager;

    @Autowired
    RuntimeStorageManager runtimeStorageManager;

    public void handleMessage(PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent){
        int ruleBaseId = platformKnowledgeBaseCreatedEvent.getRuleBaseID();
        String hostname = platformKnowledgeBaseCreatedEvent.getPlatformRuntime().getHostname();
        List<PlatformRuntime> platformRuntimeList = null;
        /**
         * first look if already stored in same machine with same RuleBaseID
         * **/
        platformRuntimeList = runtimeStorageManager.findRunningPlatformRuntime(ruleBaseId,hostname);
        if (platformRuntimeList.size()> 0){
               //TODO
        } else {
            /**
             * then look if exists somewhere else
             */
            platformRuntimeList = runtimeStorageManager.findRunningPlatformRuntime(ruleBaseId);
            if (platformRuntimeList.size()> 0){
                 //TODO
            } else {
                /**
                 * then create it as it exists no where
                 */
                //TODO
            }
        }

    }
}
