package org.chtijbug.drools.platform.persistence;


import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeFilter;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Cf. http://docs.spring.io/spring-data/jpa/docs/1.4.2.RELEASE/reference/html/jpa.repositories.html
 */
@Component
public class PlatformRuntimeInstanceRepositoryCacheService {
    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    public List<PlatformRuntimeInstance> findByRuleBaseIDAndEndDateNull(Integer ruleBaseId) {
        return platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(ruleBaseId);
    }

    public List<PlatformRuntimeInstance> findByRuleBaseIDAndShutdowDateNull(Integer ruleBaseId) {
        return platformRuntimeInstanceRepository.findByRuleBaseIDAndShutdowDateNull(ruleBaseId);
    }

    public PlatformRuntimeInstance findByRuleBaseID(Integer ruleBaseId) {
        return platformRuntimeInstanceRepository.findByRuleBaseID(ruleBaseId);
    }

    public PlatformRuntimeInstance findByRuleBaseIDAndStartDateAndEndDateNull(Integer ruleBaseId, Date startDate) {
        return platformRuntimeInstanceRepository.findByRuleBaseIDAndStartDateAndEndDateNull(ruleBaseId, startDate);
    }

    public List<PlatformRuntimeInstance> findByHostnameAndEndDateNull(String hostname) {
        return platformRuntimeInstanceRepository.findByHostnameAndEndDateNull(hostname);
    }

    public List<PlatformRuntimeInstance> findByPackageNameActiveRuntime(String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameActiveRuntime(packageName);
    }

    public List<PlatformRuntimeInstance> findByPackageNameAllRuntime(String packageName) {
        return platformRuntimeInstanceRepository.findByPackageNameAllRuntime(packageName);
    }

    public List findAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        return platformRuntimeInstanceRepository.findAllPlatformRuntimeInstanceByFilter(filter);
    }

    public Integer countAllPlatformRuntimeInstanceByFilter(PlatformRuntimeFilter filter) {
        return platformRuntimeInstanceRepository.countAllPlatformRuntimeInstanceByFilter(filter);
    }

    public void save(PlatformRuntimeInstance platformRuntimeInstance) {
        this.platformRuntimeInstanceRepository.save(platformRuntimeInstance);
    }
}
