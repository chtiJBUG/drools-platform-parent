package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 09:56
 * To change this template use File | Settings | File Templates.
 */
public interface RuleAssetRepository extends JpaRepository<RuleAsset, Long> {
    RuleAsset findByPackageNameAndAssetName(String packageName, String assetName);
}
