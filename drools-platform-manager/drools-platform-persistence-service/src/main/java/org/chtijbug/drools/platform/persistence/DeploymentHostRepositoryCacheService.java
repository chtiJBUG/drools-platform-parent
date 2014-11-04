package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.DeploymentHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 04/11/14
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DeploymentHostRepositoryCacheService {
    @Autowired
    private DeploymentHostRepository deploymentHostRepository;


    public List<DeploymentHost> findByHostName(String hostname) {
        return deploymentHostRepository.findByHostname(hostname);
    }

    public DeploymentHost findByHostnameAndPort(String hostname, Integer port) {
        return deploymentHostRepository.findByHostnameAndPort(hostname, port);

    }

    public void save(DeploymentHost deploymentHost) {
        this.deploymentHostRepository.save(deploymentHost);
    }

}
