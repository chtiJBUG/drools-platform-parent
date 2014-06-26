package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;

import java.util.List;

public interface PlatformRuntimeInstanceCustomRepository {
    List<PlatformRuntimeInstance> findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter);
}
