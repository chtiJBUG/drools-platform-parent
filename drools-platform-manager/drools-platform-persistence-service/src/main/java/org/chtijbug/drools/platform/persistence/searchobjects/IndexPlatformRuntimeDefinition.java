package org.chtijbug.drools.platform.persistence.searchobjects;

import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.gridgain.grid.cache.query.GridCacheQuerySqlField;

import java.io.Serializable;

/**
 * Created by nheron on 26/02/15.
 */
public class IndexPlatformRuntimeDefinition implements Serializable {

    @GridCacheQuerySqlField
    private Integer ruleBaseId;


    private PlatformRuntimeDefinition platformRuntimeDefinition;
    public IndexPlatformRuntimeDefinition() {
    }

    public Integer getRuleBaseId() {
        return ruleBaseId;
    }

    public void setRuleBaseId(Integer ruleBaseId) {
        this.ruleBaseId = ruleBaseId;
    }

    public PlatformRuntimeDefinition getPlatformRuntimeDefinition() {
        return platformRuntimeDefinition;
    }

    public void setPlatformRuntimeDefinition(PlatformRuntimeDefinition platformRuntimeDefinition) {
        this.platformRuntimeDefinition = platformRuntimeDefinition;
    }
}
