package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.DTRuleAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 09:56
 * To change this template use File | Settings | File Templates.
 */
public interface DTRuleAssetRepository extends JpaRepository<DTRuleAsset, Long> {
    @Query("select r from RuleAsset r,DTRuleAsset dt where dt.ruleAsset=r and r.packageName= :packageName and r.assetName=:assetName")
    DTRuleAsset findByPackageNameAndAssetName(@Param("packageName") String packageName, @Param("assetName") String assetName);
}
