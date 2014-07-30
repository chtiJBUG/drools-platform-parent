package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.DTRuleAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 09:56
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DTRuleAssetRepositoryCacheService {
    @Autowired
    DTRuleAssetRepository dtRuleAssetRepository;

    public DTRuleAsset findByPackageNameAndAssetName(String packageName, String assetName) {
        return dtRuleAssetRepository.findByPackageNameAndAssetName(packageName, assetName);
    }

    public void delete(DTRuleAsset dtruleAsset) {
        this.dtRuleAssetRepository.delete(dtruleAsset);
    }

    public void save(DTRuleAsset dtRuleAsset) {
        this.dtRuleAssetRepository.save(dtRuleAsset);
    }
}
