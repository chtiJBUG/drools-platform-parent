package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RuleRuntime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface RulesRuntimeRepository extends JpaRepository<RuleRuntime, Long> {

    @Query("select rrt from PlatformRuntime pp,SessionRuntime s,ProcessRuntime  prt,RuleflowGroupRuntime rfg , RuleRuntime rrt " +
            "where s.platformRuntime=pp and prt.sessionRuntime = s and rfg.processRuntime=prt and rrt.ruleflowGroupRuntime=rfg  " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and rfg.endDate is null and rfg.ruleflowGroup = :ruleFlowGroupName and rrt.ruleName=:ruleName")
    RuleRuntime findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId, @Param("ruleFlowGroupName") String ruleFlowGroup, @Param("ruleName") String ruleName);


    @Query("select rrt from PlatformRuntime pp,SessionRuntime s, RuleRuntime rrt " +
            "where s.platformRuntime=pp and rrt.sessionRuntime = s   " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and rrt.endDate is null")
    RuleRuntime findActiveRuleInSessionByRuleBaseIDAndSessionID(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId);

    @Query("select rrt from PlatformRuntime pp,SessionRuntime s,ProcessRuntime  prt,RuleflowGroupRuntime rfg , RuleRuntime rrt " +
            "where s.platformRuntime=pp and prt.sessionRuntime = s and rfg.processRuntime=prt and rrt.ruleflowGroupRuntime=rfg  " +
            "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and rrt.endDate is null ")
    RuleRuntime findActiveRuleInWorkflowGroupByRuleBaseIDAndSessionIDA(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionID") Integer sessionId);

}
