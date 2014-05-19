package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface PlatformRuntimeInstanceRepository extends JpaRepository<PlatformRuntimeDefinition, Long> {



    PlatformRuntimeDefinition findByRuleBaseID(Integer ruleBaseId);

}
