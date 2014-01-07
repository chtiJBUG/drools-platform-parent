package org.chtijbug.drools.platform.logger.server;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class OrientDBConnector {
    @Value("${orientdb.url}")
    private String orientdb_url;
    @Value("${orientdb.dbname}")
    private String orientdb_dbName;

    @Value("${orientdb.username}")
    private String orientdb_username;
    @Value("${orientdb.password}")
    private String orientdb_password;

    ODatabaseDocument database = null;

    public ODatabaseDocument getDatabase() {
        if (database == null) {
            database = ODatabaseDocumentPool.global().acquire(orientdb_url + orientdb_dbName, orientdb_username, orientdb_password);
        }
        return database;
    }
}
