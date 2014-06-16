package org.chtijbug.drools.platform.runtime.servlet.historylistener;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.core.droolslistener.PlatformHistoryListener;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/12/13
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ServletJmsStorageHistoryListener implements PlatformHistoryListener {

    private String platformQueueName = "historyEventQueue";
    private Integer platformPort = 61616;
    private String platformServer;

    private static final Logger LOG = Logger.getLogger(ServletJmsStorageHistoryListener.class);

    private Integer ruleBaseID;

    private MessageProducer producer;

    private Session session;


    public void setPlatformServer(String platformServer) {
        this.platformServer = platformServer;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public ServletJmsStorageHistoryListener() throws DroolsChtijbugException {

    }

    public void initJmsConnection() throws JMSException {


        String url = "tcp://" + this.platformServer + ":" + this.platformPort;
        ConnectionFactory factory = new ActiveMQConnectionFactory(url);
        try {
            Connection connection = factory.createConnection();
            session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(this.platformQueueName);
            producer = session.createProducer(queue);
        } catch (JMSException exp) {
            // TODO handle properly exception
            exp.printStackTrace();
        }
    }

    @Override
    public void fireEvent(HistoryEvent historyEvent) throws DroolsChtijbugException {
        try {
            ObjectMessage msg = session.createObjectMessage(historyEvent);
            producer.send(msg);
        } catch (JMSException e) {
            DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("JMSHistoryEvent", "FireEvent", e);
            throw droolsChtijbugException;
        }


    }

    public void shutdown() {
        final PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = new PlatformKnowledgeBaseShutdownEvent(-1, new Date(), Integer.valueOf(this.ruleBaseID).intValue(), new Date());

        try {
            this.fireEvent(platformKnowledgeBaseShutdownEvent);
            session.close();
        } catch (JMSException e) {
            LOG.error("Session Could not be closed", e);
        } catch (DroolsChtijbugException e) {
            LOG.error("Session Could not be closed", e);
        }

    }

    @Override
    protected void finalize()
            throws Throwable {
        super.finalize();

    }


}
