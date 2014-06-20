package org.chtijbug.drools.platform.backend.topic.pojo;

/**
 * Created by alexandre on 19/06/2014.
 */
public class DeploymentRequest {
    private Integer ruleBaseID;
    private String packageVersion;

    public Integer getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }
}
