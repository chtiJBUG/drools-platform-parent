package org.chtijbug.drools.platform.persistence;

import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 09:55
 * To change this template use File | Settings | File Templates.
 */
public interface RuleAssetCategoryRepository extends JpaRepository<RuleAssetCategory, Long> {

    RuleAssetCategory findBycategoryName(String categorieName);
}
