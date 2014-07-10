package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
@Transactional(readOnly = true, value = "transactionManager")
public interface PlatformRuntimeDefinitionRepository extends JpaRepository<PlatformRuntimeDefinition, Long> {


    PlatformRuntimeDefinition findByRuleBaseID(Integer ruleBaseId);

}
