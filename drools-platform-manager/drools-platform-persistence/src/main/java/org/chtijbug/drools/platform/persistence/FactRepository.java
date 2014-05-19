package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.Fact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public interface FactRepository extends JpaRepository<Fact, Long> {




    }
