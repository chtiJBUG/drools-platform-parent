package org.chtijbug.drools.platform.core.droolslistener;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseCreatedEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseReloadedEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesEndEvent;
import org.chtijbug.drools.platform.core.websocket.WebSocketServer;
import org.chtijbug.drools.platform.entity.PlatformRuntime;
import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseCreatedEvent;
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

    @Value("knowledge.rulebaseid")
    private int ruleBaseID;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {
        HistoryEvent historyEventToSend = historyEvent;
        if (historyEvent instanceof KnowledgeBaseCreatedEvent) {
            /**
             * Here we have to add all info to allow server-log to connect us
             */
            PlatformRuntime platformRuntime = new PlatformRuntime(webSocketServer.getWs_hostname(),webSocketServer.getWs_port());
            platformRuntime.setStartDate(new Date());
            platformRuntime.setEndDate(null);
            platformRuntime.setRuleBaseID(this.ruleBaseID);
            platformRuntime.setEventID(historyEvent.getEventID());
            platformRuntime.setStatus(PlatformRuntimeStatus.STARTED);
            PlatformKnowledgeBaseCreatedEvent platformKnowledgeBaseCreatedEvent = new PlatformKnowledgeBaseCreatedEvent(historyEvent.getEventID(),historyEvent.getDateEvent(),historyEvent.getRuleBaseID(),platformRuntime);
            historyEventToSend = platformKnowledgeBaseCreatedEvent;
        } else if (historyEvent instanceof KnowledgeBaseAddRessourceEvent
                || historyEvent instanceof KnowledgeBaseInitialLoadEvent
                || historyEvent instanceof KnowledgeBaseReloadedEvent) {
            if (historyEvent.getGuvnor_url() != null) {
                this.guvnor_url = historyEvent.getGuvnor_url();
                this.guvnor_appName = historyEvent.getGuvnor_appName();
                this.guvnor_packageName = historyEvent.getGuvnor_packageName();
                this.guvnor_packageVersion = historyEvent.getGuvnor_packageVersion();
            }

        } else if (this.guvnor_url != null) {
            historyEvent.setRuleBaseID(this.ruleBaseID);
            historyEvent.setGuvnor_url(this.guvnor_url);
            historyEvent.setGuvnor_appName(this.guvnor_appName);
            historyEvent.setGuvnor_packageName(this.guvnor_packageName);
            historyEvent.setGuvnor_packageVersion(this.guvnor_packageVersion);
        }


        if (historyEvent instanceof SessionFireAllRulesEndEvent) {
            //TODO send all stats to ws client
        }
        final Serializable objectToSend = historyEventToSend;
        jmsTemplate.send(new MessageCreator() {

            public Message createMessage(Session session) throws JMSException {
                ObjectMessage message = session.createObjectMessage();
                message.setObject(objectToSend);
                return message;
            }

        });

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
}