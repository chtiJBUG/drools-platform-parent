/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.slf4j.Logger;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


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
    public SessionExecution findByRuleBaseIDAndSessionIdAndEndDateIsNull(@Param("ruleBaseID") Long ruleBaseID, @Param("sessionId") Long sessionId) {
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
