package org.chtijbug.drools.platform.logger.server.jms;

import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.log4j.Logger;
import org.chtijbug.drools.platform.persistence.impl.db.OrientDBConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


/**
 * Created by IntelliJ IDEA.
 * Date: 20/12/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
@Component
public class JMSHistoryEventListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(JMSHistoryEventListener.class);

    @Autowired
    OrientDBConnector orientDBConnector ;

    public void onMessage(Message message) {
        try {

            ODocument toto = new ODocument("historyEvent");
            toto.save();
            //toto.
            /**
            OSQLSynchQuery<ODocument> query = new OSQLSynchQuery<ODocument>("");
            List<ODocument> result = database.command(query).execute();
            for (ODocument oDocument : result)  {
                oDocument.field
            }
             **/
            ObjectMessage msg = (ObjectMessage) message;
            LOG.info("Consumed message: " + msg.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}