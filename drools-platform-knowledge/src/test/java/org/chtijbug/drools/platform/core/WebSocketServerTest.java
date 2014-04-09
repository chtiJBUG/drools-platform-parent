package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.platform.core.websocket.WebSocketServer;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.net.UnknownHostException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/spring-test-config.xml"})


public class WebSocketServerTest {
    @Autowired
    static WebSocketServer webSocketServer;
    @Autowired
    DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;

    @BeforeClass
    public static void BeforeClass() throws UnknownHostException {
        webSocketServer = new WebSocketServer();
        webSocketServer.run();
    }

    @AfterClass
    public static void AfterClass() throws UnknownHostException {
        webSocketServer.stop();
    }

    @Before
    public void BeforeTest() {
        webSocketServer.setDroolsPlatformKnowledgeBase(this.droolsPlatformKnowledgeBase);

    }

    @Test
    public void testKnowledgeBaseCreate() throws DroolsChtijbugException, IOException, DeploymentException, EncodeException {
       System.out.println("coucou");
        PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
        platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.ruleVersionInfos);
        WebSocketClient webSocketClient = new WebSocketClient();
        WebSocketClientBean.getWebSocketClientBean().registerListener(new WebSocketClientListenerInterface() {
            @Override
            public void answer(PlatformManagementKnowledgeBean platformManagementKnowledgeBean) {
                System.out.println(platformManagementKnowledgeBean.toString());
            }
        });
        webSocketClient.getSession().getBasicRemote().sendObject(platformManagementKnowledgeBean);





    }


    @Test
    public void testBPMN2WorkFlowGroup() throws DroolsChtijbugException {

       ;

    }

}
