package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 09:55
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RuleAssetCategoryRepositoryCacheService {

    @Autowired
    RuleAssetCategoryRepository ruleAssetCategoryRepository;

    public RuleAssetCategory findBycategoryName(String categorieName) {
        return this.ruleAssetCategoryRepository.findBycategoryName(categorieName);

    }
}
