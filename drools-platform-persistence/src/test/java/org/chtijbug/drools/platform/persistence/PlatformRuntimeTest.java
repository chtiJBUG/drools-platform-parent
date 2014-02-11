package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.entity.pojo.PlatformRuntime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration("classpath:/spring/spring-test-persistence-config.xml" )
public class PlatformRuntimeTest {
    @Resource
    RuntimeStorageManager runtimeStorageManager ;
    @BeforeClass
    public static void BeforeClass() throws IOException {

    }

    @Test
    public void createNormal(){
        PlatformRuntime platformRuntime = new PlatformRuntime("MyPc",123);
        platformRuntime.setStartDate(new Date());
        platformRuntime.setRuleBaseID(1);
        platformRuntime.setHostname("mine");
        runtimeStorageManager.save(platformRuntime);
        PlatformRuntime platformRuntime1 = runtimeStorageManager.findRunningPlatformRuntime(1);
        Assert.assertTrue(platformRuntime1.getRuleBaseID()==platformRuntime.getRuleBaseID());
        Assert.assertTrue(platformRuntime1.getStartDate().equals(platformRuntime.getStartDate()));
        Assert.assertTrue(platformRuntime1.getEndDate() == null) ;
    }
    @Test
        public void updateNormal(){
            PlatformRuntime platformRuntime = new PlatformRuntime("MyPc",123);
            platformRuntime.setStartDate(new Date());
            runtimeStorageManager.save(platformRuntime);
            Assert.assertTrue(true);
        }

}
