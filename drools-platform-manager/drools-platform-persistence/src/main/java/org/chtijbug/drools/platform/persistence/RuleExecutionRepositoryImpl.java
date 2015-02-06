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

import org.chtijbug.drools.platform.persistence.pojo.RuleExecution;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class RuleExecutionRepositoryImpl implements RuleExecutionCustomRepository {
    private static Logger logger = getLogger(RuleflowGroupRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public RuleExecution findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName(Long ruleBaseID, Long sessionId, String ruleFlowGroup, String ruleName) {
        logger.debug(">> findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName");
        RuleExecution ruleExecutionFound = null;
        try {
            String jpaQuery = "select rrt.id, rrt.enddate, rrt.packagename, rrt.rulename, rrt.startdate, " +
                    " rrt.starteventid,rrt.stopeventid, rrt.rule_asset_id_fk, rrt.ruleflowgroup_execution_id_fk, \n" +
                    "       rrt.session_execution_id_fk \n" +
                    "from platform_runtime_instance pp,\n" +
                    "     session_execution s,\n" +
                    "     process_execution  prt,\n" +
                    "     RuleflowGroup rfg , \n" +
                    "     rule_execution rrt \n" +
                    "where s.platform_runtime_instance_id=pp.id  \n" +
                    "      and pp.endDate is null \n" +
                    "      and prt.sessionexecution_id = s.id \n" +
                    "      and rfg.process_execution_id_fk=prt.id \n" +
                    "      and rrt.ruleflowgroup_execution_id_fk=rfg.id  \n" +
                    "      and pp.ruleBaseID= :ruleBaseID \n" +
                    "      and s.sessionId =:sessionID  \n" +
                    "      and rfg.endDate is null \n" +
                    "      and rrt.endDate is null \n" +
                    "      and rfg.ruleflowGroup = :ruleFlowGroupName \n" +
                    "      and rrt.ruleName=:ruleName";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, RuleExecution.class);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionId);
            if (ruleFlowGroup != null) {
                query.setParameter("ruleFlowGroupName", ruleFlowGroup);
                query.setParameter("ruleFlowGroupName", ruleFlowGroup);
                query.setParameter("ruleName", ruleName);

                List<RuleExecution> result = query.getResultList();
                if (result.size() == 1) {
                    ruleExecutionFound = result.get(0);
                }
            }

            return ruleExecutionFound;
        } finally {
            logger.debug("<< findByRuleBaseIDAndSessionIDAndRuleFlowNameAndRuleName()");
        }
    }

    //@Override
    public RuleExecution findActiveRuleInSessionByRuleBaseIDAndSessionID(Long ruleBaseID, Long sessionId) {
        RuleExecution ruleExecutionFound = null;
        logger.debug(">> findActiveRuleInSessionByRuleBaseIDAndSessionID");
        try {
            String jpaQuery = "select rrt.id, rrt.enddate, rrt.packagename, rrt.rulename, rrt.startdate, " +
                    " rrt.starteventid,rrt.stopeventid, rrt.rule_asset_id_fk, rrt.ruleflowgroup_execution_id_fk, \n" +
                    "       rrt.session_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "     session_execution s,\n" +
                    "     rule_execution rrt \n" +
                    " where s.platform_runtime_instance_id=pp.id  \n" +
                    "      and pp.endDate is null \n" +
                    "      \n" +
                    "      and rrt.session_execution_id_fk=s.id  \n" +
                    "      and pp.ruleBaseID= :ruleBaseID \n" +
                    "      and s.sessionId =:sessionID  \n" +
                    "      and rrt.endDate is null ";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, RuleExecution.class);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionId);
            List<RuleExecution> result = query.getResultList();
            if (result.size() == 1) {
                ruleExecutionFound = result.get(0);
            }
            return ruleExecutionFound;
        } finally {
            logger.debug("<< findActiveRuleInSessionByRuleBaseIDAndSessionID()");
        }
    }

    //@Override
    public RuleExecution findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID(Long ruleBaseID, Long sessionId) {
        logger.debug(">> findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID");
        RuleExecution ruleExecutionFound = null;
        try {
            String jpaQuery = "select rrt.id, rrt.enddate, rrt.packagename, rrt.rulename, rrt.startdate, " +
                    " rrt.starteventid,rrt.stopeventid, rrt.rule_asset_id_fk, rrt.ruleflowgroup_execution_id_fk, \n" +
                    "       rrt.session_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "  session_execution s,\n" +
                    "  process_execution  prt,\n" +
                    "  ruleflowgroup rfg , \n" +
                    "  rule_execution rrt\n" +
                    " where s.platform_runtime_instance_id=pp.id\n" +
                    " and pp.endDate is null \n" +
                    " and prt.sessionexecution_id = s.id\n" +
                    " and rfg.process_execution_id_fk=prt.id \n" +
                    " and rrt.ruleflowgroup_execution_id_fk=rfg.id  \n" +
                    " and rrt.endDate is null\n" +
                    " and pp.ruleBaseID= :ruleBaseID \n" +
                    " and s.sessionId =:sessionID";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, RuleExecution.class);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionId);
            List<RuleExecution> result = query.getResultList();
            if (result.size() == 1) {
                ruleExecutionFound = result.get(0);
            }
            return ruleExecutionFound;
        } finally {
            logger.debug("<< findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID()");
        }
    }

    @Override
    public RuleExecution findActiveRuleByRuleBaseIDAndSessionIDAndRuleName(Long ruleBaseID, Long sessionId, String ruleName) {
        logger.debug(">> findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID");
        RuleExecution ruleExecutionFound = null;
        try {
            String jpaQuery = "select rrt.id, rrt.enddate, rrt.packagename, rrt.rulename, rrt.startdate, " +
                    " rrt.starteventid,rrt.stopeventid, rrt.rule_asset_id_fk, rrt.ruleflowgroup_execution_id_fk, \n" +
                    "       rrt.session_execution_id_fk \n" +
                    " from platform_runtime_instance pp,\n" +
                    "  session_execution s,\n" +
                    "  rule_execution rrt\n" +
                    " where s.platform_runtime_instance_id=pp.id\n" +
                    " and pp.endDate is null \n" +
                    " and rrt.session_execution_id_fk=s.id  \n" +
                    " and rrt.endDate is null\n" +
                    " and pp.ruleBaseID= :ruleBaseID \n" +
                    " and s.sessionId =:sessionID\n" +
                    " and rrt.rulename=:ruleName";
            //___ Append other filters
            Query query = entityManager.createNativeQuery(jpaQuery, RuleExecution.class);
            query.setParameter("ruleBaseID", ruleBaseID);
            query.setParameter("sessionID", sessionId);
            if (ruleName != null && ruleName.length() > 0) {
                query.setParameter("ruleName", ruleName);
                List<RuleExecution> result = query.getResultList();
                if (result.size() == 1) {
                    ruleExecutionFound = result.get(0);
                }
            }
            return ruleExecutionFound;
        } finally {
            logger.debug("<< findActiveRuleInRuleFlowGroupByRuleBaseIDAndSessionID()");
        }
    }
}
