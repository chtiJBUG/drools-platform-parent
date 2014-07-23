package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.*;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class PlatformRuntimeInstanceRepositoryImpl implements PlatformRuntimeInstanceCustomRepository {
    private static Logger logger = getLogger(PlatformRuntimeInstanceRepositoryImpl.class);

    private static final String SELECT_QUERY_PART = "select execution ";
    private static final String COUNT_QUERY_PART = "select count(execution) ";

    private static final String COMMON_QUERY_PART = "from SessionExecution execution , DroolsResource resource " +
            "where resource.guvnor_packageName = :packageName and " +
            "resource member of execution.platformRuntimeInstance.platformRuntimeDefinition.droolsRessourcesDefinition and " +
            "resource.endDate is null ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SessionExecution> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(filter={})", filter);
        try {
            String jpaQuery = SELECT_QUERY_PART+COMMON_QUERY_PART;
            //___ Append other filters
            Query query = appendQuerySpecs(filter, jpaQuery);

            Page page = filter.getPage();
            if(page.getMaxItemPerPage() != null){
                query.setMaxResults(page.getMaxItemPerPage());
                if(page.getCurrentIndex() != null) {
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

            String jpaQueryCount = COUNT_QUERY_PART+COMMON_QUERY_PART;
            //___ Append other filters
            Query query = appendQuerySpecs(filter, jpaQueryCount);
            //Integer cResults=(Integer) query.getFirstResult();
            int count = ((Number)query.getSingleResult()).intValue();
            return count;
        }finally {
            logger.debug("<< countAllPlatformRuntimeInstanceByFilter()");
        }
    }

    private Query appendQuerySpecs(PlatformRuntimeFilter filter, String baseQuery){
        String result = baseQuery;
        if (filter.getPackageName() == null) {
            throw new RuntimeException("The Package name is mandatory for filtering PlatformRuntimeInstance");
        }

        //___ Append other filters

        //___ runtime status
        if (filter.getStatus() != null){
            result = result.concat("and execution.platformRuntimeInstance.status=:status ");
        }

        //___ checkbox case
        if(filter.getOnlyRunningInstances()=="true"){
            result = result.concat("and execution.platformRuntimeInstance.endDate is null ");
        }

        if (filter.getHostname() != null){
            result = result.concat("and execution.platformRuntimeInstance.hostname=:hostname ");
        }
        if(filter.getStartDate() != null){
            result = result.concat("and execution.startDate>=:startdate ");
        }
        if(filter.getEndDate() != null){
            result = result.concat("and execution.endDate<=:enddate ");
        }
        Query query = entityManager.createQuery(result);
        query.setParameter("packageName", filter.getPackageName());
        if (filter.getStatus() != null){
            PlatformRuntimeInstanceStatus status= PlatformRuntimeInstanceStatus .getEnum(filter.getStatus());
            query.setParameter("status", status);
        }
        if (filter.getHostname() != null){
            query.setParameter("hostname", filter.getHostname());
        }
        if(filter.getStartDate() != null){
            query.setParameter("startdate", filter.getStartDate());
        }
        if(filter.getEndDate() != null){
            query.setParameter("enddate", filter.getEndDate());
        }
        return query;
    }

}
