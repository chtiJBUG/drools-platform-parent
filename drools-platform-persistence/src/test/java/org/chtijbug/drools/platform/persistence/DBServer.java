package org.chtijbug.drools.platform.persistence;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

/**
 * Created by IntelliJ IDEA.
 * Date: 08/01/14
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */

public class DBServer {
    private OServer server = null;

    @Override
    protected void finalize() throws Throwable {
        server.shutdown();
    }

    public DBServer() throws Exception {
        server = OServerMain.create();

        server.startup(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<orient-server>"
                        + "<network>"
                        + "<protocols>"
                        + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
                        + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
                        + "</protocols>"
                        + "<listeners>"
                        + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
                        + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
                        + "</listeners>"
                        + "</network>"
                        + "<users>"
                        + "<user name=\"root\" password=\"ThisIsA_TEST\" resources=\"*\"/>"
                        + "</users>"
                        +  "<storages>"
                        +  "<storage loaded-at-startup=\"true\" userPassword=\"admin\" userName=\"admin\" path=\"memory:droolsdb\" name=\"droolsdb\"/>"
                        +   " </storages>"
                        + "<properties>"
                         + "<entry name=\"orientdb.www.path\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/www/\"/>"
                        + "<entry name=\"orientdb.config.file\" value=\"C:/work/dev/orientechnologies/orientdb/releases/1.0rc1-SNAPSHOT/config/orientdb-server-config.xml\"/>"
                        + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
                        + "<entry name=\"log.console.level\" value=\"info\"/>"
                        + "<entry name=\"log.file.level\" value=\"fine\"/>"
                        //The following is required to eliminate an error or warning "Error on resolving property: ORIENTDB_HOME"
                        + "<entry name=\"plugin.dynamic\" value=\"false\"/>"
                        + "</properties>" + "</orient-server>");
        server.activate();
    }


}
