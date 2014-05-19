package org.chtijbug.drools.platform.persistence;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
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
        "classpath:META-INF/spring/spring-persistence-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:dataset/dataset.xml")
public class SessionRuntimeDaoTest {
    @Resource
    private SessionRuntimeRepository sessionRuntimeRepository;
    @Resource
    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Test
    public void should_get_platform_runtime_object_persisted() {
        PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseID(5);
        SessionRuntime sessionRuntime = new SessionRuntime();
        sessionRuntime.setSessionId(12);
        sessionRuntime.setStartDate(new Date());
        sessionRuntime.setPlatformRuntimeInstance(platformRuntimeInstance);
        sessionRuntimeRepository.save(sessionRuntime);
        assertThat(sessionRuntime.getId()).isNotNull();
        assertThat(sessionRuntime.getId()).isGreaterThan(0l);
    }




}
