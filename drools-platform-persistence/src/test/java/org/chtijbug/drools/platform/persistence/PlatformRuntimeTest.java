package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by IntelliJ IDEA.
 * Date: 07/01/14
 * Time: 14:03
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {"classpath:/spring/spring-test-persistence-config.xml",
        "classpath:spring/spring-persistence-context.xml"} )
public class PlatformRuntimeTest {
    @Resource
    RuntimeStorageManager runtimeStorageManager ;

    @BeforeClass
    public static void BeforeClass() throws IOException {

    }

    @Test
    public void createNormal(){
        PlatformRuntime platformRuntime = new PlatformRuntime("127.0.0.1",8080);
        platformRuntime.setStartDate(new Date());
        platformRuntime.setRuleBaseID(1);

        runtimeStorageManager.save(platformRuntime);

        PlatformRuntime platformRuntime1 = runtimeStorageManager.findRunningPlatformRuntime(1);
        Assert.assertTrue(platformRuntime1.getRuleBaseID()==platformRuntime.getRuleBaseID());
        Assert.assertTrue(platformRuntime1.getStartDate().equals(platformRuntime.getStartDate()));
        Assert.assertTrue(platformRuntime1.getEndDate() == null) ;
    }

    @Test
    public void  should_get_platform_runtime_object_persisted(){
        PlatformRuntime platformRuntime = new PlatformRuntime("MyPc",123);
        platformRuntime.setStartDate(new Date());
        runtimeStorageManager.save(platformRuntime);
        assertThat(platformRuntime.getId()).isNotNull();
    }

}
