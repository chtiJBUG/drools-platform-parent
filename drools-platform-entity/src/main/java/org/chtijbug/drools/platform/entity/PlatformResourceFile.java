package org.chtijbug.drools.platform.entity;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/05/14
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class PlatformResourceFile {
    private String fileName;
    private String content;
    private String guvnor_url=null;
    private String guvnor_appName;
    private String guvnor_packageName;
    private String guvnor_packageVersion;
    private String guvnor_userName;
    private String guvnor_password;

    public PlatformResourceFile() {
    }

    public PlatformResourceFile(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public PlatformResourceFile(String guvnor_url, String guvnor_appName, String guvnor_packageName, String guvnor_packageVersion, String guvnor_userName, String guvnor_password) {
        this.guvnor_url = guvnor_url;
        this.guvnor_appName = guvnor_appName;
        this.guvnor_packageName = guvnor_packageName;
        this.guvnor_packageVersion = guvnor_packageVersion;
        this.guvnor_userName = guvnor_userName;
        this.guvnor_password = guvnor_password;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContent() {
        return content;
    }

    public String getGuvnor_url() {
        return guvnor_url;
    }

    public String getGuvnor_appName() {
        return guvnor_appName;
    }

    public String getGuvnor_packageName() {
        return guvnor_packageName;
    }

    public String getGuvnor_packageVersion() {
        return guvnor_packageVersion;
    }

    public String getGuvnor_userName() {
        return guvnor_userName;
    }

    public String getGuvnor_password() {
        return guvnor_password;
    }
}
