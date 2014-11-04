package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.PlatformServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 04/11/14
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface PlatformServerRepository extends JpaRepository<PlatformServer, Long> {


}
