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


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class PlatformRuntimeInstanceRepositoryCacheService {
    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;


    public List<PlatformRuntimeInstance> findAll() {
        return platformRuntimeInstanceRepository.findAll();
    }

    public List<PlatformRuntimeInstance> findByRuleBaseIDAndEndDateNull(Integer ruleBaseId) {
        return platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(ruleBaseId);
    }

    public List<PlatformRuntimeInstance> findByRuleBaseIDAndShutdowDateNull(Integer ruleBaseId) {
        return platformRuntimeInstanceRepository.findByRuleBaseIDAndShutdowDateNull(ruleBaseId);
    }

    public PlatformRuntimeInstance findByRuleBaseID(Integer ruleBaseId) {
        return platformRuntimeInstanceRepository.findByRuleBaseID(ruleBaseId);
    }

    public PlatformRuntimeInstance findByRuleBaseIDAndStartDateAndEndDateNull(Integer ruleBaseId, Date startDate) {
        return platformRuntimeInstanceRepository.findByRuleBaseIDAndStartDateAndEndDateNull(ruleBaseId, startDate);
    }


    public List<PlatformRuntimeInstance> findByPackageNameActiveRuntime(String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameActiveRuntime(packageName);
    }

    public List<PlatformRuntimeInstance> findByPackageNameAllRuntime(String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameAllRuntime(packageName);
    }

    public List findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        return platformRuntimeInstanceRepository.findAllPlatformRuntimeInstanceByFilter(filter);
    }

    public Integer countAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        return platformRuntimeInstanceRepository.countAllPlatformRuntimeInstanceByFilter(filter);
    }

    public void save(PlatformRuntimeInstance platformRuntimeInstance) {
        this.platformRuntimeInstanceRepository.save(platformRuntimeInstance);
    }
}
