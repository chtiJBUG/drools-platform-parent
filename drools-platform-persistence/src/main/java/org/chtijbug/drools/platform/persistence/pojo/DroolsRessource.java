package org.chtijbug.drools.platform.persistence.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 23/01/14
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "drools_resource")
public class DroolsRessource {

    @Id
    @SequenceGenerator(name="resource_id_seq", sequenceName="drools_platform_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_id_seq")
    private Long id;
    @Column
    private String guvnor_url;
    @Column
    private String guvnor_appName;
    @Column
    private String guvnor_packageName;
    @Column
    private String guvnor_packageVersion;
    @Column
    private String fileName;
    @Column
    private String fileContent;
    @Column
    private Date startDate;
    @Column
    private Date endDate;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DroolsRessource that = (DroolsRessource) o;

        if (!guvnor_appName.equals(that.guvnor_appName)) return false;
        if (!guvnor_packageName.equals(that.guvnor_packageName)) return false;
        if (!guvnor_packageVersion.equals(that.guvnor_packageVersion)) return false;
        if (!guvnor_url.equals(that.guvnor_url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = guvnor_url.hashCode();
        result = 31 * result + guvnor_appName.hashCode();
        result = 31 * result + guvnor_packageName.hashCode();
        result = 31 * result + guvnor_packageVersion.hashCode();
        return result;
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
