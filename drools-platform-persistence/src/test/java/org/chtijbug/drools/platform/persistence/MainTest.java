package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration("classpath:/spring/spring-test-persistence-config.xml" )
public class MainTest {
    @Resource
     IPlatformRuntimeDao platformRuntimeDao;
    @BeforeClass
    public static void BeforeClass() throws IOException {

    }

    @Test
    public void pipo(){


    }
}
