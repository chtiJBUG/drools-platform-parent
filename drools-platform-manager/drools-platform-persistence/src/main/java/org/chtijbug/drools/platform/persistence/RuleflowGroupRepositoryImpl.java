package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleflowGroup;
import org.slf4j.Logger;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by IntelliJ IDEA.
 * Date: 29/07/14
 * Time: 19:06
 * To change this template use File | Settings | File Templates.
 */
public class RuleflowGroupRepositoryImpl implements RuleflowGroupCustomRepository {

    private static Logger logger = getLogger(RuleflowGroupRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    //  @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r where s.platformRuntimeInstance=pp and pp.endDate is null and p.sessionExecution=s and  r.processExecution=p and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and p.ProcessInstanceId = :processInstanceID and r.endDate is null and r.ruleflowGroup = :ruleFlowGroupName")

    @Override
    public List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(@Param("ruleBaseID") Long ruleBaseID, @Param("sessionID") Long sessionID, @Param("processInstanceID") String processInstanceID, @Param("ruleFlowGroupName") String ruleFlowGroupName) {
        logger.debug(">> findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName");
        try {
            String jpaQuery = "select r.id,  r.enddate,  r.ruleflowgroup,  r.ruleflowgroupstatus,  r.startdate,  r.starteventid, \n" +
                    "        r.stopeventid,  r.process_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "     session_execution s,\n" +
                    "     process_execution p, \n" +
                    "     ruleflowgroup r\n" +
                    " where s.platform_runtime_instance_id = pp.id\n" +
                    " and pp.enddate is null\n" +
                    " and   p.sessionexecution_id = s.id\n" +
                    " and r.process_execution_id_fk = p.id\n" +
                    " and pp.rulebaseid=:ruleBaseID\n" +
                    " and s.sessionid=:sessionID\n" +
                    " and p.processinstanceid =:processInstanceID" +
                    " and r.ruleflowgroup=:ruleFlowGroupName";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionID);
            query.setParameter("processInstanceID", processInstanceID);
            query.setParameter("ruleFlowGroupName", ruleFlowGroupName);

            return (List<RuleflowGroup>) query.getResultList();
        } finally {
            logger.debug("<< findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName()");
        }
    }

    /**
     * @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r " +
     * "where s.platformRuntimeInstance=pp  and pp.endDate is null and p.sessionExecution=s and  r.processExecution=p " +
     * "and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and p.ProcessInstanceId = :processInstanceID and r.endDate is null " +
     * "and r.ruleflowGroup = :ruleFlowGroupName")
     */
    @Override
    public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName(@Param("ruleBaseID") Long ruleBaseID, @Param("sessionID") Long sessionID, @Param("processInstanceID") String processInstanceID, @Param("ruleFlowGroupName") String ruleFlowGroupName) {
        logger.debug(">> findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName");
        try {
            String jpaQuery = "select r.id,  r.enddate,  r.ruleflowgroup,  r.ruleflowgroupstatus,  r.startdate,  r.starteventid, \n" +
                    "        r.stopeventid,  r.process_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "     session_execution s,\n" +
                    "     process_execution p, \n" +
                    "     ruleflowgroup r\n" +
                    " where s.platform_runtime_instance_id = pp.id\n" +
                    " and pp.enddate is null\n" +
                    " and   p.sessionexecution_id = s.id\n" +
                    " and r.process_execution_id_fk = p.id\n" +
                    " and pp.rulebaseid=:ruleBaseID\n" +
                    " and s.sessionid=:sessionID\n" +
                    " and p.processinstanceid =:processInstanceID" +
                    " and r.ruleflowgroup=:ruleFlowGroupName";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, RuleflowGroup.class);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionID);
            query.setParameter("processInstanceID", processInstanceID);
            query.setParameter("ruleFlowGroupName", ruleFlowGroupName);

            return (RuleflowGroup) query.getSingleResult();
        } finally {
            logger.debug("<< findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndProcessInstanceIdAndRuleflowgroupName()");
        }
    }
    //   @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r where s.platformRuntimeInstance=pp and pp.endDate is null and p.sessionExecution=s and  r.processExecution=p and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and r.endDate is null and r.ruleflowGroup = :ruleFlowGroupName")

    @Override
    public List<RuleflowGroup> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName(@Param("ruleBaseID") Long ruleBaseID, @Param("sessionID") Long sessionID, @Param("ruleFlowGroupName") String ruleFlowGroupName) {
        logger.debug(">> findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName");
        try {
            String jpaQuery = "select r.id,  r.enddate,  r.ruleflowgroup,  r.ruleflowgroupstatus,  r.startdate,  r.starteventid, \n" +
                    "        r.stopeventid,  r.process_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "     session_execution s,\n" +
                    "     process_execution p, \n" +
                    "     ruleflowgroup r\n" +
                    " where s.platform_runtime_instance_id = pp.id\n" +
                    " and pp.enddate is null\n" +
                    " and   p.sessionexecution_id = s.id\n" +
                    " and r.process_execution_id_fk = p.id\n" +
                    " and pp.rulebaseid=:ruleBaseID\n" +
                    " and s.sessionid=:sessionID\n" +
                    " and r.ruleflowgroup=:ruleFlowGroupName";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionID);
            query.setParameter("ruleFlowGroupName", ruleFlowGroupName);

            return (List<RuleflowGroup>) query.getResultList();
        } finally {
            logger.debug("<< findAllStartedRuleFlowGroupByRuleBaseIDAndSessionAndRuleflowgroupName()");
        }
    }
    //   @Query("select r from PlatformRuntimeInstance pp,SessionExecution s,ProcessExecution p,RuleflowGroup r where s.platformRuntimeInstance=pp and pp.endDate is null  and p.sessionExecution=s and  r.processExecution=p and pp.ruleBaseID= :ruleBaseID and s.sessionId =:sessionID  and r.endDate is null and r.ruleflowGroup = :ruleFlowGroupName")

    @Override
    public RuleflowGroup findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName(@Param("ruleBaseID") Long ruleBaseID, @Param("sessionID") Long sessionID, @Param("ruleFlowGroupName") String ruleFlowGroupName) {
        logger.debug(">> findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName");
        try {
            String jpaQuery = "select r.id,  r.enddate,  r.ruleflowgroup,  r.ruleflowgroupstatus,  r.startdate,  r.starteventid, \n" +
                    "        r.stopeventid,  r.process_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "     session_execution s,\n" +
                    "     process_execution p, \n" +
                    "     ruleflowgroup r\n" +
                    " where s.platform_runtime_instance_id = pp.id\n" +
                    " and pp.enddate is null\n" +
                    " and   p.sessionexecution_id = s.id\n" +
                    " and r.process_execution_id_fk = p.id\n" +
                    " and pp.rulebaseid=:ruleBaseID\n" +
                    " and s.sessionid=:sessionID\n" +
                    " and r.ruleflowgroup=:ruleFlowGroupName";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, RuleflowGroup.class);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionID);
            query.setParameter("ruleFlowGroupName", ruleFlowGroupName);

            return (RuleflowGroup) query.getSingleResult();
        } finally {
            logger.debug("<< findStartedRuleFlowGroupByRuleBaseIDAndSessionIDAndRuleflowgroupName()");
        }
    }
}
