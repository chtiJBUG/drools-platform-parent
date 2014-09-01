package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class PlatformRuntimeDefinitionRepositoryCacheService {
    @Autowired
    PlatformRuntimeDefinitionRepository platformRuntimeDefinitionRepository;

    public PlatformRuntimeDefinition findByRuleBaseID(Integer ruleBaseId) {
        return this.platformRuntimeDefinitionRepository.findByRuleBaseID(ruleBaseId);
    }

    public void save(PlatformRuntimeDefinition platformRuntimeDefinition) {
        this.platformRuntimeDefinitionRepository.save(platformRuntimeDefinition);
    }
}
