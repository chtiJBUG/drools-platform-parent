package org.chtijbug.drools.platform.persistence.internals;

import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceCustomRepository;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

import static org.slf4j.LoggerFactory.*;

public class PlatformRuntimeInstanceCustomRepositoryImpl implements PlatformRuntimeInstanceCustomRepository {
    private static Logger logger = getLogger(PlatformRuntimeInstanceCustomRepositoryImpl.class);
    private static final String BASE_JPA_QUERY = "select pri " +
            "from PlatformRuntimeInstance pri,PlatformRuntimeDefinition prd ,DroolsResource drs " +
            "where pri.platformRuntimeDefinition = prd " +
            "and drs member of prd.droolsRessourcesDefinition " +
            "and drs.guvnor_packageName=:packageName ";

    @PersistenceUnit
    private EntityManager entityManager;

    @Override
    public List<PlatformRuntimeInstance> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        logger.debug(">> findAllPlatformRuntimeInstanceByFilter(filter={})", filter);
        try {

            if (filter.getpackageName() == null)
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
            query.setParameter("packageName", filter.getpackageName());
            query.setParameter("status", filter.getStatus());
            query.setParameter("hostname", filter.getHostname());
            query.setParameter("startDate", filter.getStartDate());
            query.setParameter("endDate", filter.getEndDate());



            //___TODO append other filter criteria for the jpaQuery


            return query.getResultList();
        } finally {
            logger.debug("<< findAllPlatformRuntimeInstanceByFilter()");
        }
    }


}
