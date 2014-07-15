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

    List<PlatformRuntimeInstance> findByRuleBaseIDAndEndDateNull(Integer ruleBaseId);

    List<PlatformRuntimeInstance> findByRuleBaseIDAndShutdowDateNull(Integer ruleBaseId);

    PlatformRuntimeInstance findByRuleBaseID(Integer ruleBaseId);

    PlatformRuntimeInstance findByRuleBaseIDAndStartDateAndEndDateNull(Integer ruleBaseId, Date startDate);

    public List<PlatformRuntimeInstance> findByHostnameAndEndDateNull(String hostname);

    @Query(value = "select pri " +
            "from PlatformRuntimeInstance pri,PlatformRuntimeDefinition prd ,DroolsResource drs " +
            "where pri.platformRuntimeDefinition = prd " +
            "and drs member of prd.droolsRessourcesDefinition " +
            "and pri.endDate is null " +
            "and drs.guvnor_packageName=:packageName")
    public List<PlatformRuntimeInstance> findByPackageNameActiveRuntime(@Param("packageName") String packageName);


    @Query(value = "select pri " +
            "from PlatformRuntimeInstance pri,PlatformRuntimeDefinition prd ,DroolsResource drs " +
            "where pri.platformRuntimeDefinition = prd " +
            "and drs member of prd.droolsRessourcesDefinition " +
            "and drs.guvnor_packageName=:packageName")
    public List<PlatformRuntimeInstance> findByPackageNameAllRuntime(@Param("packageName") String packageName);

}
