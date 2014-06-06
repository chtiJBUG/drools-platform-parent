package org.chtijbug.drools.platform.web;

/**
 * Created by alexandre on 06/06/2014.
 */
public class RuntimeInstance {
    private Long id;
    private String url;
    private String rulePackage;
    private String version;

    public RuntimeInstance() {/* nop */}

    public RuntimeInstance(Long id, String url, String rulePackage, String version) {
        this.id = id;
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
