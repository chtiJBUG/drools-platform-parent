package org.chtijbug.drools.platform.web.model;

/**
 * Created by alexandre on 06/06/2014.
 */
public class RuntimeInstance {
    private Long id;
    private Integer ruleBaseId;
    private String url;
    private String rulePackage;
    private String version;

    public RuntimeInstance() {/* nop */}

    public RuntimeInstance(Long id, Integer ruleBaseId, String url, String rulePackage, String version) {
        this.id = id;
        this.ruleBaseId = ruleBaseId;
        this.url = url;
        this.rulePackage = rulePackage;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRuleBaseId() {
        return ruleBaseId;
    }

    public void setRuleBaseId(Integer ruleBaseId) {
        this.ruleBaseId = ruleBaseId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(String rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
