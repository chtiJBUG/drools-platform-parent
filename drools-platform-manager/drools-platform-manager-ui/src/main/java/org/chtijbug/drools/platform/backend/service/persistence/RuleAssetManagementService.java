/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.backend.service.persistence;

import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.guvnor.rest.model.AssetCategory;
import org.chtijbug.drools.platform.persistence.RuleAssetCategoryRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.RuleAssetRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Component
public class RuleAssetManagementService {

    @Autowired
    RuleAssetRepositoryCacheService ruleAssetRepository;

    @Autowired
    RuleAssetCategoryRepositoryCacheService ruleAssetCategoryRepository;

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


