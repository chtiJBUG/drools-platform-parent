package org.chtijbug.drools.platform.core.websocket;

import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.entity.ChtijbugRuleBaseReportBean;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static java.lang.System.out;  
  
@ServerEndpoint(value="/echo",  
                encoders = {ChtijbugRuleBaseReportBean.ChtijbugRuleBaseReportBeanCode.class},
                decoders = {ChtijbugRuleBaseReportBean.ChtijbugRuleBaseReportBeanCode.class})
public class RuntimeServerService
{

    private Session peerLoggerServer;

    private static final Logger LOG = Logger.getLogger(RuntimeServerService.class);
    @OnMessage  
    public void echo (ChtijbugRuleBaseReportBean bean, Session peer) throws IOException, EncodeException {
        //  
       // bean.setReply("Server says " + bean.getMessage());
        this.peerLoggerServer = peer;
        LOG.info("Websocket initialized with server logger");


    }  

    public void sendMessAgeToServer(ChtijbugRuleBaseReportBean bean) throws IOException, EncodeException {
        this.peerLoggerServer.getBasicRemote().sendObject(bean);

    }

    @OnOpen  
    public void onOpen(final Session session, EndpointConfig endpointConfig) {  
        out.println("Server connected "  + session + " " + endpointConfig);  
    }  
}  