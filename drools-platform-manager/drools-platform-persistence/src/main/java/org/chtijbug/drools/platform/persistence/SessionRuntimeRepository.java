package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.SessionRuntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface SessionRuntimeRepository extends JpaRepository<SessionRuntime, Long> {


    @Query("select s from PlatformRuntime pp,SessionRuntime s " +
             "where s.platformRuntime=pp " +
             "and  pp.ruleBaseID= :ruleBaseID and s.sessionId = :sessionID  " +
             "and s.endDate is null ")
    SessionRuntime findByRuleBaseIDAndSessionIdAndEndDateIsNull(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId);



}
