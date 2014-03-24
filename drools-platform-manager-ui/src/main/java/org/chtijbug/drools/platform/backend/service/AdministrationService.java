package org.chtijbug.drools.platform.backend.service;

import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 24/03/14
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AdministrationService {

    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;


}
