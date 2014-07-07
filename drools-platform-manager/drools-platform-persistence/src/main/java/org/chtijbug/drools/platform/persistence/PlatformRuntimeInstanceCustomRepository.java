package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;

import java.util.List;

public interface PlatformRuntimeInstanceCustomRepository {
    List<SessionExecution> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter);
}
