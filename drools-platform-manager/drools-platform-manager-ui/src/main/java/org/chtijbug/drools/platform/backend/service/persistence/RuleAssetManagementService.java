package org.chtijbug.drools.platform.backend.service.persistence;

import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.guvnor.rest.model.AssetCategory;
import org.chtijbug.drools.platform.persistence.RuleAssetCategoryRepository;
import org.chtijbug.drools.platform.persistence.RuleAssetRepository;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    RuleAssetRepository ruleAssetRepository;

    @Autowired
    RuleAssetCategoryRepository ruleAssetCategoryRepository;

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


    private RuleAsset getRuleAssetWithCategory(String packageName, String assetName, List<String> categoryNameListFromGuvnor, Integer versionNumber) {
        RuleAsset ruleAsset = null;
        if (packageName != null && assetName != null) {
            RuleAsset searchRuleAsset = ruleAssetRepository.findByPackageNameAndAssetName(packageName, assetName);
            if (searchRuleAsset != null) {
                if (searchRuleAsset.getVersionNumber() == null) {
                    searchRuleAsset.setVersionNumber(new Integer("-1"));
                }
                if (searchRuleAsset.getVersionNumber() < versionNumber) {

                    ruleAsset = searchRuleAsset;
                    ruleAsset.setVersionNumber(versionNumber);
                    if (ruleAsset.getRuleAssetCategory() == null) {
                        ruleAsset.setRuleAssetCategory(new ArrayList<RuleAssetCategory>());
                    }
                    for (String categoryName : categoryNameListFromGuvnor) {
                        ruleAssetCategoryToInsertForRuleAsset(ruleAsset, categoryName);
                    }
                    List<RuleAssetCategory> ruleAssetCategoriesToRemove = new ArrayList<>();
                    for (RuleAssetCategory ruleAssetCategory : ruleAsset.getRuleAssetCategory()) {
                        if (!categoryNameListFromGuvnor.contains(ruleAssetCategory.getCategoryName())) {
                            ruleAssetCategoriesToRemove.add(ruleAssetCategory);
                        }
                    }
                    for (RuleAssetCategory e : ruleAssetCategoriesToRemove) {
                        ruleAsset.getRuleAssetCategory().remove(e);
                    }
                    ruleAssetRepository.save(ruleAsset);
                }

            } else {
                ruleAsset = new RuleAsset(packageName, assetName);
                ruleAsset.setVersionNumber(versionNumber);
                for (String categoryName : categoryNameListFromGuvnor) {
                    ruleAssetCategoryToInsertForRuleAsset(ruleAsset, categoryName);
                }
                ruleAssetRepository.save(ruleAsset);
            }
        }
        return ruleAsset;
    }

    private void ruleAssetCategoryToInsertForRuleAsset(RuleAsset ruleAsset, String categoryName) {
        boolean found = false;
        for (RuleAssetCategory ruleAssetCategory : ruleAsset.getRuleAssetCategory()) {
            if (ruleAssetCategory.getCategoryName() != null && ruleAssetCategory.getCategoryName().equals(categoryName)) {
                found = true;
            }
        }
        if (found == false) {
            RuleAssetCategory ruleAssetCategory = ruleAssetCategoryRepository.findBycategoryName(categoryName);
            if (ruleAssetCategory == null) {
                ruleAssetCategory = new RuleAssetCategory(categoryName);
            }
            ruleAsset.getRuleAssetCategory().add(ruleAssetCategory);
        }
    }

    @Transactional
    public void synchronizeInDBGuvnorCategories(String packageName, Asset asset) {
        String assetName = asset.getName();
        List<String> assetCategorylist = new ArrayList<>();
        for (AssetCategory element : asset.getCategories()) {
            String assetCategory = element.getName();
            if (assetCategory != null && assetCategory.length() > 0) {
                assetCategorylist.add(assetCategory);
            }
        }
        this.getRuleAssetWithCategory(packageName, assetName, assetCategorylist, new Integer(asset.getVersionNumber()));
    }


}


