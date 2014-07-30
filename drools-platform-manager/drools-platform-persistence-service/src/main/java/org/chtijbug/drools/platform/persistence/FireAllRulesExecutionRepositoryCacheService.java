package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class FireAllRulesExecutionRepositoryCacheService {
    @Autowired
    FireAllRulesExecutionRepository fireAllRulesExecutionRepository;

    public List<FireAllRulesExecution> findAllStartedFireAllRulesBySessionID(Integer sessionID) {

        return fireAllRulesExecutionRepository.findAllStartedFireAllRulesBySessionID(sessionID);
    }


    public FireAllRulesExecution findStartedFireAllRulesBySessionID(Integer sessionID) {
        return fireAllRulesExecutionRepository.findStartedFireAllRulesBySessionID(sessionID);
    }


    public void save(FireAllRulesExecution fireAllRulesExecution) {
        this.fireAllRulesExecutionRepository.save(fireAllRulesExecution);
    }
}
