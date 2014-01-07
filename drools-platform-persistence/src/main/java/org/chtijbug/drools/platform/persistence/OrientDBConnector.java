package org.chtijbug.drools.platform.persistence;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OSQLHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
@Component
public class OrientDBConnector {
    @Value("${orientdb.url}")
    private String orientdb_url;
    @Value("${orientdb.dbname}")
    private String orientdb_dbName;

    @Value("${orientdb.username}")
    private String orientdb_username;
    @Value("${orientdb.password}")
    private String orientdb_password;

    ODatabaseDocumentTx database = null;

    public ODatabaseDocument getDatabase() {
        if (database == null) {
            database = ODatabaseDocumentPool.global().acquire(orientdb_url + orientdb_dbName, orientdb_username, orientdb_password);
        }
        //ODatabaseDocumentTx toto = new ODatabaseDocumentTx(orientdb_url + orientdb_dbName).open(orientdb_username,orientdb_password) ;

        return database;
    }
   public void beginTransaction(){
       this.database.begin();
   }
   public void commitTransaction(){
       this.database.commit();
   }

   public void rollbackTransaction(){
       this.database.rollback();
   }
   public ODocument getDocument(ORID id){
       return this.database.load(id);
   }
    public ODocument getDocument(String id){
        return this.database.load((ORecordId) OSQLHelper.parseValue(id,null));
    }
}
