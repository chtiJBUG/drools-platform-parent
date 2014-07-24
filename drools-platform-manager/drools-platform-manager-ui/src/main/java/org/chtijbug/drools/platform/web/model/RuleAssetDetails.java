package org.chtijbug.drools.platform.web.model;

import org.chtijbug.drools.platform.persistence.pojo.RuleAssetCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandre on 24/07/2014.
 */
public class RuleAssetDetails {
    private String assetName;
    private Integer versionNumber;
    private List<RuleAssetCategory> ruleAssetCategory = new ArrayList<>();

    public RuleAssetDetails() { /* nope */ }

    public RuleAssetDetails(String assetName, Integer versionNumber, List<RuleAssetCategory> ruleAssetCategory) {
        this.assetName = assetName;
        this.versionNumber = versionNumber;
        this.ruleAssetCategory = ruleAssetCategory;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public List<RuleAssetCategory> getRuleAssetCategory() {
        return ruleAssetCategory;
    }

    public void setRuleAssetCategory(List<RuleAssetCategory> ruleAssetCategory) {
        this.ruleAssetCategory = ruleAssetCategory;
    }
}
