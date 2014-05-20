package org.chtijbug.drools.platform.persistence;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.chtijbug.drools.platform.entity.util.DateHelper;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
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
        "classpath:META-INF/spring/spring-persistence-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:dataset/dataset.xml")
public class PlatformRuntimeInstanceDaoTest {
    @Resource
    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Test
    public void should_get_platform_runtime_object_persisted() {
        PlatformRuntimeInstance platformRuntimeInstance = new PlatformRuntimeInstance("MyPc", 123);
        platformRuntimeInstance.setStartDate(new Date());
        platformRuntimeInstanceRepository.save(platformRuntimeInstance);
        assertThat(platformRuntimeInstance.getId()).isNotNull();
        assertThat(platformRuntimeInstance.getId()).isGreaterThan(0l);
    }

    @Test
    public void should_find_an_active_platform_by_ruleBaseID() {
        PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseID(5);
        assertThat("192.168.1.18").isEqualTo(platformRuntimeInstance.getHostname());
        assertThat(platformRuntimeInstance.getEndDate()).isNull();
    }

    @Test
    public void should_get_a_platform_resolved_by_ruleBaseID_and_startDate() throws Exception {
        PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseIDAndStartDateAndEndDateNull(5, DateHelper.getDate("2014-02-12"));
        assertThat("192.168.1.18").isEqualTo(platformRuntimeInstance.getHostname());
        assertThat(platformRuntimeInstance.getEndDate()).isNull();
    }

    @Test
    public void should_get_2_runtimes_found_per_hostname() throws Exception {
        List<PlatformRuntimeInstance> platformRuntimelistInstance = platformRuntimeInstanceRepository.findByHostnameAndEndDateNull("192.168.1.18");
        assertThat(platformRuntimelistInstance).hasSize(2);
    }
}
