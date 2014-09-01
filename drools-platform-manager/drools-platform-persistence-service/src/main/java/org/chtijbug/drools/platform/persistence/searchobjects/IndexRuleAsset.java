package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Date: 31/07/14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class IndexRuleAsset implements Serializable {

    @GridCacheQuerySqlField
    private String packageName;

    @GridCacheQuerySqlField
    private String assetName;


    private RuleAsset ruleAsset;

    public IndexRuleAsset() {
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public RuleAsset getRuleAsset() {
        return ruleAsset;
    }

    public void setRuleAsset(RuleAsset ruleAsset) {
        this.ruleAsset = ruleAsset;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndexRuleAsset{");
        sb.append("packageName='").append(packageName).append('\'');
        sb.append(", ruleName=").append(assetName);
        sb.append(", ruleAsset=").append(ruleAsset);
        sb.append('}');
        return sb.toString();
    }
}
