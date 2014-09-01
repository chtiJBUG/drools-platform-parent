package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class RealTimeParametersRepositoryCacheServcice {
    @Autowired
    RealTimeParametersRepository realTimeParametersRepository;

    public RealTimeParameters findById(Long id) {
        return this.realTimeParametersRepository.findById(id);
    }


}
