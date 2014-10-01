/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.persistence;


import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/spring-test-persistence-config.xml",
        "classpath:META-INF/spring/spring-persistence-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("classpath:dataset/dataset.xml")
public class SessionExecutionDaoTest {
    @Resource
    private SessionExecutionRepository sessionExecutionRepository;
    @Resource
    private PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Test
    public void should_get_platform_runtime_object_persisted() {
        PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseID(5);
        SessionExecution sessionExecution = new SessionExecution();
        sessionExecution.setSessionId(12);
        sessionExecution.setStartDate(new Date());
        sessionExecution.setPlatformRuntimeInstance(platformRuntimeInstance);
        sessionExecutionRepository.save(sessionExecution);
        assertThat(sessionExecution.getId()).isNotNull();
        assertThat(sessionExecution.getId()).isGreaterThan(0l);
    }


}
