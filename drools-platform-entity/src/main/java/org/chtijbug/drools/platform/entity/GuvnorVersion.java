package org.chtijbug.drools.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by IntelliJ IDEA.
 * Date: 24/03/14
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuvnorVersion {

    private String guvnor_url;
    private String guvnor_appName;
    private String guvnor_packageName;
    private String guvnor_packageVersion;

    public GuvnorVersion() {
    }

    public GuvnorVersion(String guvnor_url, String guvnor_appName, String guvnor_packageName, String guvnor_packageVersion) {
        this.guvnor_url = guvnor_url;
        this.guvnor_appName = guvnor_appName;
        this.guvnor_packageName = guvnor_packageName;
        this.guvnor_packageVersion = guvnor_packageVersion;
    }

    public String getGuvnor_url() {
        return guvnor_url;
    }

    public void setGuvnor_url(String guvnor_url) {
        this.guvnor_url = guvnor_url;
    }

    public String getGuvnor_appName() {
        return guvnor_appName;
    }

    public void setGuvnor_appName(String guvnor_appName) {
        this.guvnor_appName = guvnor_appName;
    }

    public String getGuvnor_packageName() {
        return guvnor_packageName;
    }

    public void setGuvnor_packageName(String guvnor_packageName) {
        this.guvnor_packageName = guvnor_packageName;
    }

    public String getGuvnor_packageVersion() {
        return guvnor_packageVersion;
    }

    public void setGuvnor_packageVersion(String guvnor_packageVersion) {
        this.guvnor_packageVersion = guvnor_packageVersion;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GuvnorVersion{");
        sb.append("guvnor_url='").append(guvnor_url).append('\'');
        sb.append(", guvnor_appName='").append(guvnor_appName).append('\'');
        sb.append(", guvnor_packageName='").append(guvnor_packageName).append('\'');
        sb.append(", guvnor_packageVersion='").append(guvnor_packageVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
