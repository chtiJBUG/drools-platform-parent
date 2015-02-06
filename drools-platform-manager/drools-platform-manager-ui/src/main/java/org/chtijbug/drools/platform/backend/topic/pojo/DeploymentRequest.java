package org.chtijbug.drools.platform.backend.topic.pojo;

/**
 * Created by alexandre on 19/06/2014.
 */
public class DeploymentRequest {
    private Long ruleBaseID;
    private String groupId;
    private String artifactId;
    private String version;

    public Long getRuleBaseID() {
        return ruleBaseID;
    }

    public void setRuleBaseID(Long ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
