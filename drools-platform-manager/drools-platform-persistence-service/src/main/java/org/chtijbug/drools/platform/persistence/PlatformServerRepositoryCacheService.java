package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 04/11/14
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PlatformServerRepositoryCacheService {
    @Autowired
    PlatformServerRepository platformServerRepository;

    public List<PlatformServer> findAll() {
        return platformServerRepository.findAll();
    }

    public void save(PlatformServer platformServer) {
        this.platformServerRepository.save(platformServer);
    }
}
