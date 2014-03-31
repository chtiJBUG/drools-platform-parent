package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.FireRulesRuntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface FireRulesRuntimeRepository extends JpaRepository<FireRulesRuntime, Long> {

    @Query("select r from FireRulesRuntime r,SessionRuntime s where r.sessionRuntime = s and r.endDate is null and s.sessionId = :sessionID")
    List<FireRulesRuntime> findAllStartedFireAllRulesBySessionID(@Param("sessionID") Integer sessionID);

    @Query("select r from FireRulesRuntime r,SessionRuntime s where r.sessionRuntime = s and r.endDate is null and s.sessionId = :sessionID")
    FireRulesRuntime findStartedFireAllRulesBySessionID(@Param("sessionID") Integer sessionID);




}
