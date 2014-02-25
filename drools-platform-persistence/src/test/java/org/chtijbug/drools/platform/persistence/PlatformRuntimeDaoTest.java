package org.chtijbug.drools.platform.persistence;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.chtijbug.drools.platform.entity.util.DateHelper;
import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
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
import java.util.List;

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
public class PlatformRuntimeDaoTest {
    @Resource
    private IPlatformRuntimeDao platformRuntimeDao;

    @Test
    public void should_get_platform_runtime_object_persisted() {
        PlatformRuntime platformRuntime = new PlatformRuntime("MyPc", 123);
        platformRuntime.setStartDate(new Date());
        platformRuntimeDao.save(platformRuntime);
        assertThat(platformRuntime.getId()).isNotNull();
        assertThat(platformRuntime.getId()).isGreaterThan(0l);
    }

    @Test
    public void should_find_an_active_platform_by_ruleBaseID() {
        PlatformRuntime platformRuntime = platformRuntimeDao.findbyActivePlatformByRulebaseID(5);
        assertThat("192.168.1.18").isEqualTo(platformRuntime.getHostname());
        assertThat(platformRuntime.getEndDate()).isNull();
    }

    @Test
    public void should_get_a_platform_resolved_by_ruleBaseID_and_startDate() throws Exception {
        PlatformRuntime platformRuntime = platformRuntimeDao.findByRuleBaseIdAndStartDate(5, DateHelper.getDate("2014-02-12"));
        assertThat("192.168.1.18").isEqualTo(platformRuntime.getHostname());
        assertThat(platformRuntime.getEndDate()).isNull();
    }

    @Test
    public void should_get_2_runtimes_found_per_hostname() throws Exception {
        List<PlatformRuntime> platformRuntimelist = platformRuntimeDao.findActiveByHostName("192.168.1.18");
        assertThat(platformRuntimelist).hasSize(2);
    }
}
