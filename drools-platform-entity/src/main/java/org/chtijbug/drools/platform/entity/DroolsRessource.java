package org.chtijbug.drools.platform.entity;

/**
 * Created by IntelliJ IDEA.
 * Date: 23/01/14
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public class DroolsRessource {

    public static String class_name="droolsressource";
    public static String var_url="guvnor_url";
    public static String var_appName="guvnor_appName";
    public static String var_packageName="guvnor_packageName";
    public static String var_packageVersion="guvnor_packageVersion";
    public static String var_fileName="filename";
    public static String var_fileContent="filecontent";
    private String guvnor_url;
    private String guvnor_appName;
    private String guvnor_packageName;
    private String guvnor_packageVersion;
    private String fileName;
    private String fileContent;

    public DroolsRessource() {
    }

    public DroolsRessource(String guvnor_url, String guvnor_appName, String guvnor_packageName, String guvnor_packageVersion) {
        this.guvnor_url = guvnor_url;
        this.guvnor_appName = guvnor_appName;
        this.guvnor_packageName = guvnor_packageName;
        this.guvnor_packageVersion = guvnor_packageVersion;
    }

    public DroolsRessource(String fileName, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
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

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DroolsRessource{");
        sb.append("guvnor_url='").append(guvnor_url).append('\'');
        sb.append(", guvnor_appName='").append(guvnor_appName).append('\'');
        sb.append(", guvnor_packageName='").append(guvnor_packageName).append('\'');
        sb.append(", guvnor_packageVersion='").append(guvnor_packageVersion).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", fileContent='").append(fileContent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
