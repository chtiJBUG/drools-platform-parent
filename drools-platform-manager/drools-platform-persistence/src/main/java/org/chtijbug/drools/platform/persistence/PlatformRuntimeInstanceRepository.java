package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface PlatformRuntimeInstanceRepository extends JpaRepository<PlatformRuntimeInstance, Long> {

    List<PlatformRuntimeInstance> findByRuleBaseIDAndEndDateNull(Integer ruleBaseId);

    List<PlatformRuntimeInstance>  findByRuleBaseIDAndShutdowDateNull(Integer ruleBaseId);

    PlatformRuntimeInstance findByRuleBaseID(Integer ruleBaseId);

    PlatformRuntimeInstance findByRuleBaseIDAndStartDateAndEndDateNull(Integer ruleBaseId, Date startDate);

    public List<PlatformRuntimeInstance> findByHostnameAndEndDateNull(String hostname);
}
