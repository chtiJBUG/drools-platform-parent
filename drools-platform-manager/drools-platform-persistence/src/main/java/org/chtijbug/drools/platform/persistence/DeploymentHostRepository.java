package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.DeploymentHost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 04/11/14
 * Time: 14:10
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface DeploymentHostRepository extends JpaRepository<DeploymentHost, Long> {

    public List<DeploymentHost> findByHostname(String hostname);

    public DeploymentHost findByHostnameAndPort(String hostname, Integer port);
}
