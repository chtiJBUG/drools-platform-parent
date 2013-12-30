package org.chtijbug.drools.platform.logger.server.jms;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;


/**
 * Created by IntelliJ IDEA.
 * Date: 20/12/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class JMSHistoryEventListener implements MessageListener {

    private static final Logger LOG = Logger.getLogger(JMSHistoryEventListener.class);


    @Value("${orientdb.url}")
    private String orientdb_url;
    @Value("${guvnor.dbname}")
    private String orientdb_dbName;

    @Value("${orientdb.username}")
    private String orientdb_username;
    @Value("${orientdb.password}")
    private String orientdb_password;

    ODatabaseDocument database=null;

    public void onMessage(Message message) {
        try {
            if (database== null){
                database = ODatabaseDocumentPool.global().acquire(orientdb_url+orientdb_dbName,orientdb_username,orientdb_password);
            }
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