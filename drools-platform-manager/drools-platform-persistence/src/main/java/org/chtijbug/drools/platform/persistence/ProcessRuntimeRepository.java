package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.ProcessRuntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface ProcessRuntimeRepository extends JpaRepository<ProcessRuntime, Long> {

    @Query("select r from ProcessRuntime r,SessionRuntime s where r.sessionRuntime = s and r.endDate is null and s.sessionId = :sessionID and r.ProcessInstanceId = :processInstanceId")
    List<ProcessRuntime> findAllStartedProcessBySessionIDAndProcessInstanceId(@Param("sessionID") Integer sessionID, @Param("processInstanceId") String processInstanceId);

    @Query("select r from ProcessRuntime r,SessionRuntime s where r.sessionRuntime = s and r.endDate is null and s.sessionId = :sessionID  and r.ProcessInstanceId = :processInstanceId")
    ProcessRuntime findStartedProcessBySessionIDAndProcessInstanceId(@Param("sessionID") Integer sessionID, @Param("processInstanceId") String processInstanceId);
}
