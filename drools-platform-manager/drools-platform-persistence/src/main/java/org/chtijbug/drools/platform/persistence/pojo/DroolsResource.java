/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.persistence.pojo;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "drools_resource", indexes = {@Index(columnList = "guvnor_packageName")})
@Cacheable(value = true)
public class DroolsResource {

    @Id
    @SequenceGenerator(name = "resource_id_seq", sequenceName = "drools_platform_seq")
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
    @Lob
    @Column(length = 10000)
    private String fileContent;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    private Integer startEventID;

    private Integer stopEventID;


    public DroolsResource() {
    }

    public DroolsResource(String guvnor_url, String guvnor_appName, String guvnor_packageName, String guvnor_packageVersion) {
        this.guvnor_url = guvnor_url;
        this.guvnor_appName = guvnor_appName;
        this.guvnor_packageName = guvnor_packageName;
        this.guvnor_packageVersion = guvnor_packageVersion;
    }

    public DroolsResource(String fileName, String fileContent) {
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

    public void setGuvnor_packageVersion(String guvnor_packageVersion) {
        this.guvnor_packageVersion = guvnor_packageVersion;
    }

    public Integer getStartEventID() {
        return startEventID;
    }

    public void setStartEventID(Integer startEventID) {
        this.startEventID = startEventID;
    }

    public Integer getStopEventID() {
        return stopEventID;
    }

    public void setStopEventID(Integer stopEventID) {
        this.stopEventID = stopEventID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DroolsResource that = (DroolsResource) o;

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
        final StringBuffer sb = new StringBuffer("DroolsResource{");
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
