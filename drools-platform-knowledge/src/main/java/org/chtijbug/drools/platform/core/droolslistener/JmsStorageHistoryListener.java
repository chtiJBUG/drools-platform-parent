package org.chtijbug.drools.platform.core.droolslistener;


import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.websocket.WebSocketServer;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.mbeans.RuleBaseSupervision;
import org.chtijbug.drools.runtime.mbeans.StatefulSessionSupervision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/12/13
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
@Component
public class JmsStorageHistoryListener implements HistoryListener {

    private String guvnor_url = null;
    private String guvnor_appName;
    private String guvnor_packageName;
    private String guvnor_packageVersion;

    private RuleBaseSupervision mbsRuleBase;
    private StatefulSessionSupervision mbsSession;

    private static final Logger LOG = Logger.getLogger(JmsStorageHistoryListener.class);

    private Date startDate;


    @Value("${knowledge.rulebaseid}")
    private String ruleBaseID;

    @Autowired
    private JmsTemplate jmsTemplate;

    private DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;

    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {


        final Serializable objectToSend = historyEvent;
        jmsTemplate.send(new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(objectToSend);
                return message;
            }

        });

    }

    public void shutdown() {
        final PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = new PlatformKnowledgeBaseShutdownEvent(-1, this.startDate, Integer.valueOf(this.ruleBaseID).intValue(), new Date());
        jmsTemplate.send(new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(platformKnowledgeBaseShutdownEvent);
                return message;
            }

        });

        // this.activeMQConnectionFactor
    }

    @Override
    protected void finalize()
            throws Throwable {
        super.finalize();

    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    public void setMbsRuleBase(RuleBaseSupervision mbsRuleBase) {
        this.mbsRuleBase = mbsRuleBase;
    }

    public void setMbsSession(StatefulSessionSupervision mbsSession) {
        this.mbsSession = mbsSession;
    }

    public void setDroolsPlatformKnowledgeBase(DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase) throws UnknownHostException {
        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;


    }
}
