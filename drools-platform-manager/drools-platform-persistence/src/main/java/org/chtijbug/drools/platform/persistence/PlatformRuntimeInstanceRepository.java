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


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface PlatformRuntimeInstanceRepository extends JpaRepository<PlatformRuntimeInstance, Long>, PlatformRuntimeInstanceCustomRepository {

    List<PlatformRuntimeInstance> findByRuleBaseIDAndEndDateNull(Long ruleBaseId);

    List<PlatformRuntimeInstance> findByRuleBaseIDAndShutdowDateNull(Long ruleBaseId);

    PlatformRuntimeInstance findByRuleBaseID(Long ruleBaseId);

    PlatformRuntimeInstance findByRuleBaseIDAndStartDateAndEndDateNull(Long ruleBaseId, Date startDate);


    @Query(value = "select pri " +
            "from PlatformRuntimeInstance pri,PlatformRuntimeDefinition prd ,DroolsResource drs " +
            "where pri.platformRuntimeDefinition = prd " +
            "and drs member of prd.droolsRessourcesDefinition " +
            "and pri.endDate is null " +
            "and drs.version=:packageName")
    public List<PlatformRuntimeInstance> findByPackageNameActiveRuntime(@Param("packageName") String packageName);


    @Query(value = "select pri " +
            "from PlatformRuntimeInstance pri,PlatformRuntimeDefinition prd ,DroolsResource drs " +
            "where pri.platformRuntimeDefinition = prd " +
            "and drs member of prd.droolsRessourcesDefinition " +
            "and drs.version=:packageName")
    public List<PlatformRuntimeInstance> findByPackageNameAllRuntime(@Param("packageName") String packageName);

}
