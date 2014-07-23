package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;

import java.util.List;

public interface PlatformRuntimeInstanceCustomRepository {
    List findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter);

    Integer countAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter);
}
