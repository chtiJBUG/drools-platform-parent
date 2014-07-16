package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstanceStatus;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class PlatformRuntimeInstanceRepositoryImpl implements PlatformRuntimeInstanceCustomRepository {
    private static Logger logger = getLogger(PlatformRuntimeInstanceRepositoryImpl.class);
    private static final String BASE_JPA_QUERY = "select execution " +
            "from SessionExecution execution , DroolsResource resource " +
            "where resource.guvnor_packageName = :packageName and " +
            "resource member of execution.platformRuntimeInstance.platformRuntimeDefinition.droolsRessourcesDefinition and " +
            "resource.endDate is null ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SessionExecution> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(filter={})", filter);
        try {
            String jpaQuery = BASE_JPA_QUERY;
            if (filter.getPackageName() == null) {
                throw new RuntimeException("The Package name is mandatory for filtering PlatformRuntimeInstance");
            }

            //___ Append other filters

            //___ runtime status
            if (filter.getStatus() != null){
                jpaQuery = jpaQuery.concat("and execution.platformRuntimeInstance.status=:status ");
            }

            //___ checkbox case
            if(filter.getOnlyRunningInstances()=="true"){
                jpaQuery = jpaQuery.concat("and execution.platformRuntimeInstance.endDate is null ");
            }

            if (filter.getHostname() != null){
                jpaQuery = jpaQuery.concat("and execution.platformRuntimeInstance.hostname=:hostname ");
            }
            if(filter.getStartDate() != null){
                jpaQuery = jpaQuery.concat("and execution.startDate>=:startdate ");
            }
            if(filter.getEndDate() != null){
                jpaQuery = jpaQuery.concat("and execution.endDate<=:enddate ");
            }


            Query query = entityManager.createQuery(jpaQuery);

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
            //___TODO append other filter criteria for the jpaQuery


            return query.getResultList();
        } finally {
            logger.debug("<< findAllPlatformRuntimeInstanceByFilter()");
        }
    }


}
