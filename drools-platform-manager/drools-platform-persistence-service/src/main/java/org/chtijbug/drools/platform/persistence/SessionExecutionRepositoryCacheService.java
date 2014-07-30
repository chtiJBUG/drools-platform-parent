package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class SessionExecutionRepositoryCacheService {
    @Autowired
    SessionExecutionRepository sessionExecutionRepository;

    public SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(Integer ruleBaseID, Integer sessionId) {
        return this.sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(ruleBaseID, sessionId);
    }

    public SessionExecution findDetailsBySessionId(@Param("sessionId") Integer sessionId) {
        return this.sessionExecutionRepository.findDetailsBySessionId(sessionId);
    }


    public void save(SessionExecution sessionExecution) {
        this.sessionExecutionRepository.save(sessionExecution);
    }
}
