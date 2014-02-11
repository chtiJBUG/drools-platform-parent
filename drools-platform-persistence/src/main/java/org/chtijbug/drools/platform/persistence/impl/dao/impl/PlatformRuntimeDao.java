package org.chtijbug.drools.platform.persistence.impl.dao.impl;

import org.chtijbug.drools.platform.entity.pojo.PlatformRuntime;
import org.chtijbug.drools.platform.persistence.impl.dao.IPlatformRuntimeDao;
import org.chtijbug.drools.platform.persistence.impl.db.AbstractJpaDAO;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/02/14
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */

    @Repository
    public class PlatformRuntimeDao extends AbstractJpaDAO<PlatformRuntime> implements IPlatformRuntimeDao {

       public PlatformRuntimeDao(){
          setClazz(PlatformRuntime.class );
       }

    }

