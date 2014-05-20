package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface RuleExecutionRepository extends JpaRepository<RuleExecution, Long> {

    @Query("select rrt from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution  prt,RuleflowGroup rfg , RuleExecution rrt " +
            "where s.platformRuntimeInstance=pp  and pp.endDate is null and prt.sessionExecution = s and rfg.processExecution=prt and rrt.ruleflowGroup=rfg  " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and rfg.endDate is null and rrt.endDate is null and rfg.ruleflowGroup = :ruleFlowGroupName and rrt.ruleName=:ruleName")
    RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId, @Param("ruleFlowGroupName") String ruleFlowGroup, @Param("ruleName") String ruleName);


    @Query("select rrt from PlatformRuntimeInstance pp,SessionExecution s, RuleExecution rrt " +
            "where s.platformRuntimeInstance=pp  and pp.endDate is null and rrt.sessionExecution = s   " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and rrt.endDate is null")
    RuleExecution findActiveRuleInSessionByRuleBaseIDAndSessionID(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId);


    @Query("select rrt from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution  prt,RuleflowGroup rfg , RuleExecution rrt " +
            "where s.platformRuntimeInstance=pp  and pp.endDate is null and prt.sessionExecution = s and rfg.processExecution=prt and rrt.ruleflowGroup=rfg  " +
            "and rrt.endDate is null and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  ")
    RuleExecution findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId);




    @Query("select rrt from PlatformRuntimeInstance pp,SessionExecution s, RuleExecution rrt " +
            "where s.platformRuntimeInstance=pp  and pp.endDate is null and rrt.sessionExecution=s  " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and rrt.ruleName =:ruleName and rrt.endDate is null ")
    RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId, @Param("ruleName") String ruleName);

}
