package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.slf4j.Logger;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by IntelliJ IDEA.
 * Date: 30/07/14
 * Time: 13:54
 * To change this template use File | Settings | File Templates.
 */
public class SessionExecutionRepositoryImpl implements SessionExecutionCustomRepository {
    private static Logger logger = getLogger(SessionExecutionRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param ruleBaseID
     * @param sessionId
     * @return
     * @Query("select s from SessionExecution s " +
     * "where s.platformRuntimeInstance.ruleBaseID= :ruleBaseID and s.sessionId = :sessionId  "+
     * "and s.platformRuntimeInstance.endDate is null  ")
     */

    @Override
    public SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(@Param("ruleBaseID") Integer ruleBaseID, @Param("sessionId") Integer sessionId) {
        logger.debug(">> findByRuleBaseIDAndSessionIdAndEndDateIsNull");
        SessionExecution sessionExecutionFound = null;
        try {
            String jpaQuery = "SELECT s.id, s.enddate, s.sessionexecutionstatus, s.sessionid, s.startdate, s.starteventid, \n" +
                    "  s.stopeventid, s.platform_runtime_instance_id\n" +
                    "  FROM session_execution s, platform_runtime_instance p \n" +
                    "  where s.platform_runtime_instance_id = p.id\n" +
                    "  and s.sessionid=:sessionid\n" +
                    "  and p.rulebaseid = :rulebaseID\n" +
                    "  and p.enddate is null";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, SessionExecution.class);
            query.setParameter("rulebaseID", ruleBaseID);
            query.setParameter("sessionid", sessionId);
            List<SessionExecution> result = query.getResultList();
            if (result.size() == 1) {
                sessionExecutionFound = result.get(0);
            }

            return sessionExecutionFound;
        } finally {
            logger.debug("<< findByRuleBaseIDAndSessionIdAndEndDateIsNull()");
        }
    }
}
