package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface FireAllRulesExecutionRepository extends JpaRepository<FireAllRulesExecution, Long> {

    @Query("select r from FireAllRulesExecution r,SessionExecution s where r.sessionExecution = s and s.endDate is null and r.endDate is null and s.sessionId = :sessionID")
    List<FireAllRulesExecution> findAllStartedFireAllRulesBySessionID(@Param("sessionID") Integer sessionID);

    @Query("select r from FireAllRulesExecution r,SessionExecution s where r.sessionExecution = s and s.endDate is null and  r.endDate is null and s.sessionId = :sessionID")
    FireAllRulesExecution findStartedFireAllRulesBySessionID(@Param("sessionID") Integer sessionID);




}
