package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class PlatformRuntimeInstanceRepositoryImpl implements PlatformRuntimeInstanceCustomRepository {
    private static Logger logger = getLogger(PlatformRuntimeInstanceRepositoryImpl.class);
    private static final String BASE_JPA_QUERY = "select pri " +
            "from PlatformRuntimeInstance pri,PlatformRuntimeDefinition prd ,DroolsResource drs " +
            "where pri.platformRuntimeDefinition = prd " +
            "and drs member of prd.droolsRessourcesDefinition " +
            "and drs.guvnor_packageName=:packageName ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PlatformRuntimeInstance> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(filter={})", filter);
        try {

            if (filter.getPackageName() == null)
                throw new RuntimeException("The Package name is mandatory for filtering PlatformRuntimeInstance");
            String jpaQuery = BASE_JPA_QUERY;

            //___ Append other filter
            if(filter.getStatus() != null)
                jpaQuery = jpaQuery.concat("and pri.status=:status ");

            if(filter.getHostname() != null)
                jpaQuery += jpaQuery.concat("and pri.hostname=:hostname ");

            if(filter.getStartDate() != null)
                jpaQuery = jpaQuery.concat("and drs.startDate=:startDate ");

            if(filter.getEndDate() != null)
                jpaQuery = jpaQuery.concat("and drs.endDate=:endDate ");

            Query query = entityManager.createQuery(jpaQuery);
            query.setParameter("packageName", filter.getPackageName());
            /*query.setParameter("status", filter.getStatus());
            query.setParameter("hostname", filter.getHostname());
            query.setParameter("startDate", filter.getStartDate());
            query.setParameter("endDate", filter.getEndDate());*/



            //___TODO append other filter criteria for the jpaQuery


            return query.getResultList();
        } finally {
            logger.debug("<< findAllPlatformRuntimeInstanceByFilter()");
        }
    }


}
