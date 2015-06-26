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

import org.chtijbug.drools.platform.persistence.pojo.Page;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstanceStatus;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class PlatformRuntimeInstanceRepositoryImpl implements PlatformRuntimeInstanceCustomRepository {
    private static final String SELECT_QUERY_PART = "select new org.chtijbug.drools.platform.persistence.pojo.SessionExecutionRecord(execution.id,execution.sessionId,execution.startDate,execution.endDate,execution.platformRuntimeMode,execution.processingStartDate,execution.processingStopDate,execution.platformRuntimeInstance) ";
    private static final String COUNT_QUERY_PART = "select count(execution) ";
    private static final String COMMON_QUERY_PART = "from SessionExecutionRecord as execution , DroolsResource resource " +
            "where resource.guvnor_packageName = :packageName and " +
            "resource member of execution.platformRuntimeInstance.droolsRessources and " +
            "resource.endDate is null ";
    private static Logger logger = getLogger(PlatformRuntimeInstanceRepositoryImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SessionExecution> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(filter={})", filter);
        try {
            String jpaQuery = SELECT_QUERY_PART + COMMON_QUERY_PART;
            //___ Append other filters
            Query query = appendQuerySpecs(filter, jpaQuery, true);

            Page page = filter.getPage();
            if (page.getMaxItemPerPage() != null) {
                query.setMaxResults(page.getMaxItemPerPage());
                if (page.getCurrentIndex() != null) {
                    query.setFirstResult((page.getCurrentIndex() - 1) * page.getMaxItemPerPage());
                }
            }
            return query.getResultList();
        } finally {
            logger.debug("<< findAllPlatformRuntimeInstanceByFilter()");
        }
    }


    @Override
    public Integer countAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        logger.debug(">> countAllPlatformRuntimeInstanceByFilter(filter={})", filter);
        try {

            String jpaQueryCount = COUNT_QUERY_PART + COMMON_QUERY_PART;
            //___ Append other filters
            Query query = appendQuerySpecs(filter, jpaQueryCount, false);
            //Integer cResults=(Integer) query.getFirstResult();
            int count = ((Number) query.getSingleResult()).intValue();
            return count;
        } finally {
            logger.debug("<< countAllPlatformRuntimeInstanceByFilter()");
        }
    }

    private Query appendQuerySpecs(PlatformRuntimeFilter filter, String baseQuery, boolean isOrdered) {
        String result = baseQuery;
        if (filter.getPackageName() == null) {
            throw new RuntimeException("The Package name is mandatory for filtering PlatformRuntimeInstance");
        }

        //___ Append other filters

        //___ runtime status
        if (filter.getStatus() != null) {
            result = result.concat("and execution.platformRuntimeInstance.status=:status ");
        }

        //___ checkbox case
        if (filter.getOnlyRunningInstances() == "true") {
            result = result.concat("and execution.platformRuntimeInstance.endDate is null ");
        }

        if (filter.getHostname() != null) {
            result = result.concat("and execution.platformRuntimeInstance.hostname=:hostname ");
        }
        if (filter.getStartDate() != null) {
            result = result.concat("and execution.startDate>=:startdate ");
        }
        if (filter.getEndDate() != null) {
            result = result.concat("and execution.endDate<=:enddate ");
        }

        if (isOrdered) {
            // Try to sort the list by sessionId and start date of session execution
            result = result.concat("order by execution.sessionId ASC, execution.startDate ");

        }

        Query query = entityManager.createQuery(result);
        query.setParameter("packageName", filter.getPackageName());
        if (filter.getStatus() != null) {
            PlatformRuntimeInstanceStatus status = PlatformRuntimeInstanceStatus.getEnum(filter.getStatus());
            query.setParameter("status", status);
        }
        if (filter.getHostname() != null) {
            query.setParameter("hostname", filter.getHostname());
        }
        if (filter.getStartDate() != null) {
            query.setParameter("startdate", filter.getStartDate());
        }
        if (filter.getEndDate() != null) {
            query.setParameter("enddate", filter.getEndDate());
        }
        return query;
    }

}
