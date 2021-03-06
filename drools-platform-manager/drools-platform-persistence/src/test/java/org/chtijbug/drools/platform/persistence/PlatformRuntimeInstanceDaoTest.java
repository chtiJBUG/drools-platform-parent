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
import org.chtijbug.drools.platform.entity.util.DateHelper;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;


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
    @Ignore
    public void should_find_an_active_platform_by_ruleBaseID() {
        PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseID(5);
        //     assertThat("192.168.1.18").isEqualTo(platformRuntimeInstance.getHostname());
        assertThat(platformRuntimeInstance.getEndDate()).isNull();
    }

    @Test
    @Ignore
    public void should_get_a_platform_resolved_by_ruleBaseID_and_startDate() throws Exception {
        PlatformRuntimeInstance platformRuntimeInstance = platformRuntimeInstanceRepository.findByRuleBaseIDAndStartDateAndEndDateNull(5, DateHelper.getDate("2014-02-12"));
        //   assertThat("192.168.1.18").isEqualTo(platformRuntimeInstance.getHostname());
        assertThat(platformRuntimeInstance.getEndDate()).isNull();
    }


}
