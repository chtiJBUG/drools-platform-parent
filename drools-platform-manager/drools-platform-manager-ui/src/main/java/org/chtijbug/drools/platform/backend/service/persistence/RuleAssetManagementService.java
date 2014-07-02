package org.chtijbug.drools.platform.backend.service.persistence;

import com.google.common.base.Throwables;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.guvnor.rest.model.AssetCategory;
import org.chtijbug.drools.platform.persistence.RuleAssetRepository;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 01/07/14
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RuleAssetManagementService {


    GuvnorRepositoryConnector guvnorRepositoryConnector;

    @Autowired
    RuleAssetRepository ruleAssetRepository;

    @Autowired
    public RuleAssetManagementService(RuntimeSiteTopology runtimeSiteTopology) {
        guvnorRepositoryConnector = new GuvnorRepositoryConnector(runtimeSiteTopology.buildGuvnorConfiguration());
    }

    public RuleAsset getRuleAsset(String packageName, String assetName) {
        RuleAsset ruleAsset = null;

        if (packageName != null && assetName == null) {
            RuleAsset searchRuleAsset = ruleAssetRepository.findByPackageNameAndAssetName(packageName, assetName);
            if (searchRuleAsset != null) {
                ruleAsset = searchRuleAsset;
            } else {
                ruleAsset = new RuleAsset(packageName, assetName);
                ruleAssetRepository.save(ruleAsset);
            }
        }
        return ruleAsset;
    }


    public RuleAsset getRuleAssetWithCategory(String packageName, String assetName, List<String> categoryNameList) {
        RuleAsset ruleAsset = null;
        if (packageName != null && assetName != null) {
            RuleAsset searchRuleAsset = ruleAssetRepository.findByPackageNameAndAssetName(packageName, assetName);
            if (searchRuleAsset != null) {
                ruleAsset = searchRuleAsset;
                if (ruleAsset.getRuleAssetCategory() == null) {
                    ruleAsset.setRuleAssetCategory(new ArrayList<RuleAssetCategory>());
                }
                if (ruleAsset.getRuleAssetCategory().size() == 0) {
                    for (String categoryName : categoryNameList) {
                        RuleAssetCategoryToInsertForRuleAsset(ruleAsset, categoryName);
                    }
                    ruleAssetRepository.save(ruleAsset);
                } else {
                    for (String categoryName : categoryNameList) {
                        RuleAssetCategoryToInsertForRuleAsset(ruleAsset, categoryName);
                    }
                    ruleAssetRepository.save(ruleAsset);
                }
            } else {
                ruleAsset = new RuleAsset(packageName, assetName);
                for (String categoryName : categoryNameList) {
                    RuleAssetCategoryToInsertForRuleAsset(ruleAsset, categoryName);
                }
                ruleAssetRepository.save(ruleAsset);
            }
        }
        return ruleAsset;
    }

    private void RuleAssetCategoryToInsertForRuleAsset(RuleAsset ruleAsset, String categoryName) {
        boolean found = false;
        for (RuleAssetCategory ruleAssetCategory : ruleAsset.getRuleAssetCategory()) {
            if (ruleAssetCategory.getCategoryName() != null && ruleAssetCategory.getCategoryName().equals(categoryName)) {
                found = true;
            }
        }
        if (found = false) {
            RuleAssetCategory ruleAssetCategory = new RuleAssetCategory(categoryName);
            ruleAsset.getRuleAssetCategory().add(ruleAssetCategory);
        }
    }

    @Scheduled(cron = "* */15 9-18 *  * MON-FRI ")
    public void SynchronizeGuvnorCategories() {
        try {
            List<Asset> listPackages = guvnorRepositoryConnector.getAllPackagesInGuvnorRepo();
            for (Asset packageAsset : listPackages) {
                String packageName = packageAsset.getName();
                List<Asset> assetList = guvnorRepositoryConnector.getAllBusinessAssets(packageName);
                for (Asset asset : assetList) {
                    String assetName = asset.getName();
                    List<String> assetCategorylist = new ArrayList<>();
                    for (AssetCategory element : asset.getCategories()) {
                        String assetCategory = element.getName();
                        if (assetCategory != null && assetCategory.length() > 0) {
                            assetCategorylist.add(assetCategory);
                        }
                    }
                    this.getRuleAssetWithCategory(packageName, assetName, assetCategorylist);
                }
            }
        } catch (ChtijbugDroolsRestException e) {
            throw Throwables.propagate(e);
        }

    }

}
