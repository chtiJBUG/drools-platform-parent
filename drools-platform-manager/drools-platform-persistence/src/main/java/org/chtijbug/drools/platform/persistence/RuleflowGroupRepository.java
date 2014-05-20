package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface RuleflowGroupRepository extends JpaRepository<RuleflowGroup, Long> {


    @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r where s.platformRuntimeInstance=pp and pp.endDate is null and p.sessionExecution=s and  r.processExecution=p and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and p.ProcessInstanceId = :processInstanceID and r.endDate is null and r.ruleflowGroup = :ruleFlowGroupName")
    List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("processInstanceID") String processInstanceID, @Param("ruleFlowGroupName") String ruleFlowGroupName);

    @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r " +
            "where s.platformRuntimeInstance=pp  and pp.endDate is null and p.sessionExecution=s and  r.processExecution=p " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and p.ProcessInstanceId = :processInstanceID and r.endDate is null " +
            "and r.ruleflowGroup = :ruleFlowGroupName")
    RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("processInstanceID") String processInstanceID, @Param("ruleFlowGroupName") String ruleFlowGroupName);


    @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r where s.platformRuntimeInstance=pp and pp.endDate is null and p.sessionExecution=s and  r.processExecution=p and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and r.endDate is null and r.ruleflowGroup = :ruleFlowGroupName")
    List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("ruleFlowGroupName") String ruleFlowGroupName);

    @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r where s.platformRuntimeInstance=pp and pp.endDate is null  and p.sessionExecution=s and  r.processExecution=p and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and r.endDate is null and r.ruleflowGroup = :ruleFlowGroupName")
    RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionID, @Param("ruleFlowGroupName") String ruleFlowGroupName);


}
