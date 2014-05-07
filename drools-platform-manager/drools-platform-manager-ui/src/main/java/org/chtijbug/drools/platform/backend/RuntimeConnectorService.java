package org.chtijbug.drools.platform.backend;

import org.chtijbug.drools.platform.backend.wsclient.WebSocketClient;
import org.chtijbug.drools.platform.backend.wsclient.WebSocketSessionManager;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 06/05/14
 * Time: 18:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RuntimeConnectorService {

    @Autowired
    private WebSocketSessionManager webSocketSessionManager;

    @Autowired
    private PlatformRuntimeRepository platformRuntimeRepository;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public void updateRulePackage(Integer ruleBaseID, String packageVersion) throws DroolsChtijbugException {

        WebSocketClient clientSocket = webSocketSessionManager.getWebSocketClient(ruleBaseID);
        if (clientSocket == null) {
            DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("updateRulePackage-unknowrulebaseid", ruleBaseID.toString(), null);
            throw droolsChtijbugException;
        } else {
            PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
            platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.loadNewRuleVersion);
            List<PlatformRuntime> platformRuntimeList = platformRuntimeRepository.findByRuleBaseIDAndEndDateNull(ruleBaseID);
            if (platformRuntimeList.size() != 1) {
                DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("updateRulePackage-NoUniqueRuleBaseID", ruleBaseID.toString(), null);
                throw droolsChtijbugException;
            }
        }


    }

}
