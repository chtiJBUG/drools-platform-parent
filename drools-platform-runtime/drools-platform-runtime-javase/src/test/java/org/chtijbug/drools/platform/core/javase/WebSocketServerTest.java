package org.chtijbug.drools.platform.core.javase;

import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.junit.Test;

import javax.websocket.DeploymentException;
import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


public class WebSocketServerTest {


    DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;
    private static String ws_hostname;
    private static int ws_port;


    //@Test
    public void testKnowledgeBaseCreate() throws DroolsChtijbugException, IOException, DeploymentException, EncodeException, InterruptedException {
        final Semaphore doWait = new Semaphore(1);
        doWait.acquire();

        WebSocketClientBean.webSocketClientListenerInterface = new WebSocketClientListenerInterface() {
            @Override
            public void answer(PlatformManagementKnowledgeBean platformManagementKnowledgeBean) {
                System.out.println("Client Side event Listener");
                System.out.println(platformManagementKnowledgeBean.toString());
                assertThat(platformManagementKnowledgeBean.getRequestStatus()).isEqualTo(RequestStatus.SUCCESS);
                doWait.release();
                /** assertThat(platformManagementKnowledgeBean.getResourceFileList().size()==1).isNotNull();
                 assertThat(platformManagementKnowledgeBean.getgetGuvnorVersion()).isNotNull();
                 assertThat(platformManagementKnowledgeBean.getGuvnorVersion().getGuvnor_url()).isEqualTo("http://localhost:8080/");
                 assertThat(platformManagementKnowledgeBean.getGuvnorVersion().getGuvnor_appName()).isEqualTo("drools-guvnor");
                 assertThat(platformManagementKnowledgeBean.getGuvnorVersion().getGuvnor_packageName()).isEqualTo("test");
                 assertThat(platformManagementKnowledgeBean.getGuvnorVersion().getGuvnor_packageVersion()).isEqualTo("LATEST"); **/

            }
        };

        WebSocketClient webSocketClient = new WebSocketClient();
        webSocketClient.setName("WebSocketClient");
        webSocketClient.start();
        //webSocketClient.run();


        PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
        platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.ruleVersionInfos);
        System.out.println(platformManagementKnowledgeBean.toString());

        webSocketClient.getSession().getBasicRemote().sendObject(platformManagementKnowledgeBean);
        System.out.println("Bean Sent");
        //doWait.acquire();
        doWait.tryAcquire(30, TimeUnit.SECONDS);

        webSocketClient.end();

    }


    @Test
    public void testBPMN2WorkFlowGroup() throws DroolsChtijbugException {

        ;

    }

}