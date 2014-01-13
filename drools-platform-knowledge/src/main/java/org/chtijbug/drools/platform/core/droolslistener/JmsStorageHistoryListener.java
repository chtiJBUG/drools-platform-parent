package org.chtijbug.drools.platform.core.droolslistener;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseInitialLoadEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseReloadedEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesEndEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.mbeans.RuleBaseSupervision;
import org.chtijbug.drools.runtime.mbeans.StatefulSessionSupervision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

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

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {

        if (historyEvent instanceof KnowledgeBaseAddRessourceEvent
                || historyEvent instanceof KnowledgeBaseInitialLoadEvent
                || historyEvent instanceof KnowledgeBaseReloadedEvent) {
            if (historyEvent.getGuvnor_url() != null) {
                this.guvnor_url = historyEvent.getGuvnor_url();
                this.guvnor_appName = historyEvent.getGuvnor_appName();
                this.guvnor_packageName = historyEvent.getGuvnor_packageName();
                this.guvnor_packageVersion = historyEvent.getGuvnor_packageVersion();
            }

        } else {
            if (this.guvnor_url != null) {
                historyEvent.setGuvnor_url(this.guvnor_url);
                historyEvent.setGuvnor_appName(this.guvnor_appName);
                historyEvent.setGuvnor_packageName(this.guvnor_packageName);
                historyEvent.setGuvnor_packageVersion(this.guvnor_packageVersion);
            }

        }
        if (historyEvent instanceof SessionFireAllRulesEndEvent){
           //TODO send all stats to ws client
        }
        final Serializable objectToSend = historyEvent;
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
