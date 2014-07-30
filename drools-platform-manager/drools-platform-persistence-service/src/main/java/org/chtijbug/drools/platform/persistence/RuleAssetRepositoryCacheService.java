package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 09:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RuleAssetRepositoryCacheService {
    @Autowired
    RuleAssetRepository ruleAssetRepository;

    public RuleAsset findByPackageNameAndAssetName(String packageName, String assetName) {
        return this.ruleAssetRepository.findByPackageNameAndAssetName(packageName, assetName);
    }

    public void save(RuleAsset ruleAsset) {
        this.ruleAssetRepository.save(ruleAsset);
    }
}
