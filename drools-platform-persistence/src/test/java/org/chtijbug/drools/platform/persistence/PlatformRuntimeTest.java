package org.chtijbug.drools.platform.persistence;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.annotation.Resource;
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
        "classpath:spring/spring-persistence-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:dataset/dataset.xml")
public class PlatformRuntimeTest {
    @Resource
    private IPlatformRuntimeDao platformRuntimeDao;


    @Before
    public void before() {

    }


    @Test
    public void createNormal() {
        PlatformRuntime platformRuntime = new PlatformRuntime("pipohost", 8080);
        platformRuntime.setStartDate(new Date());
        platformRuntime.setRuleBaseID(18);
        platformRuntimeDao.save(platformRuntime);
        PlatformRuntime platformRuntime1 = platformRuntimeDao.findbyActivePlatformByRulebaseID(18);
        assertThat(platformRuntime1.getStartDate()).isEqualTo(platformRuntime.getStartDate());
        assertThat(platformRuntime1).isNotNull();

    }

    @Test
    public void should_get_platform_runtime_object_persisted() {
        PlatformRuntime platformRuntime = new PlatformRuntime("MyPc", 123);
        platformRuntime.setStartDate(new Date());
        platformRuntimeDao.save(platformRuntime);
        assertThat(platformRuntime.getId()).isNotNull();
    }

}
