package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface SessionExecutionRepository extends JpaRepository<SessionExecution, Long> {


    @Query("select s from PlatformRuntimeInstance pp,SessionExecution s " +
             "where s.platformRuntimeInstance=pp " +
             "and  pp.ruleBaseID= :ruleBaseID and s.sessionId = :sessionId  "+
             "and pp.endDate is null  ")
    SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionId") Integer sessionId);


    public SessionExecution findDetailsBySessionId(@Param("sessionId") Integer sessionId);



}
