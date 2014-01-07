package org.chtijbug.drools.platform.persistence;

import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.record.impl.ODocument;
import org.chtijbug.drools.platform.persistence.impl.db.OrientDBConnector;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration("file:src/main/resources/spring/spring-persistence-context.xml" )
public class MainTest {
    @Resource
    OrientDBConnector orientDBConnector;
    @BeforeClass
    public static void BeforeClass() throws IOException {
        File toto = File.createTempFile("orientdbCacheFolder", "");
        toto.delete();
        toto.mkdirs();
        System.setProperty("basedir", toto.getAbsolutePath());
    }

    @Test
    public void pipo(){
         this.orientDBConnector.toString();
       ODatabaseRecordThreadLocal.INSTANCE.set(this.orientDBConnector.getDatabase());
        this.orientDBConnector.beginTransaction();
        ODocument toto = new ODocument("historyEvent");
        toto.field("name","putain de nombisdsdssdssd");

        toto.save();
        orientDBConnector.commitTransaction();
        ODocument result = this.orientDBConnector.getDocument(toto.getIdentity());
        System.out.println(result.toString());
        orientDBConnector.getDatabase().close();
    }
}
