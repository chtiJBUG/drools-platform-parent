package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface PlatformRuntimeRepository extends JpaRepository<PlatformRuntime, Long> {

    PlatformRuntime findByRuleBaseIDAndEndDateNull(Integer ruleBaseId);

    PlatformRuntime findByRuleBaseIDAndShutdowDateNull(Integer ruleBaseId);

    PlatformRuntime findByRuleBaseID(Integer ruleBaseId);

    PlatformRuntime findByRuleBaseIDAndStartDateAndEndDateNull(Integer ruleBaseId, Date startDate);

    public List<PlatformRuntime> findByHostnameAndEndDateNull(String hostname);
}
