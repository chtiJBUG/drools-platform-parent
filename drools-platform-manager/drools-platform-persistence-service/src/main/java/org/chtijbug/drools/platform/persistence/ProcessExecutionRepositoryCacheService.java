package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.ProcessExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class ProcessExecutionRepositoryCacheService {
    @Autowired
    ProcessExecutionRepository processExecutionRepository;

    public List<ProcessExecution> findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(Integer ruleBaseID, Integer sessionID, String processInstanceId) {
        return this.processExecutionRepository.findAllStartedProcessByRuleBaseIDAndSessionIDAndProcessInstanceId(ruleBaseID, sessionID, processInstanceId);
    }


    public ProcessExecution findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(Integer ruleBaseID, Integer sessionID, String processInstanceId) {

        return this.processExecutionRepository.findStartedProcessByRuleBaseIDBySessionIDAndProcessInstanceId(ruleBaseID, sessionID, processInstanceId);

    }

    public void save(ProcessExecution processExecution) {
        this.processExecutionRepository.save(processExecution);
    }
}
