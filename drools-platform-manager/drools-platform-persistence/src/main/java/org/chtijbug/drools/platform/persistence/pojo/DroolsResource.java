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
    private String groupId;
    @Column
    private String artifactID;
    @Column
    private String version;

    @Column
    private String fileName;
    @Lob
    @Column(length = 10000)
    private String fileContent;
    @Column
    private Date startDate;
    @Column
    private Date endDate;
    private Long startEventID;

    private Long stopEventID;


    public DroolsResource() {
    }

    public DroolsResource(String guvnor_url, String groupId, String artifactID, String version) {
        this.guvnor_url = guvnor_url;
        this.groupId = groupId;
        this.artifactID = artifactID;
        this.version = version;
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

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactID() {
        return artifactID;
    }

    public String getVersion() {
        return version;
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

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getStartEventID() {
        return startEventID;
    }

    public void setStartEventID(Long startEventID) {
        this.startEventID = startEventID;
    }

    public Long getStopEventID() {
        return stopEventID;
    }

    public void setStopEventID(Long stopEventID) {
        this.stopEventID = stopEventID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DroolsResource that = (DroolsResource) o;

        if (guvnor_url != null ? !guvnor_url.equals(that.guvnor_url) : that.guvnor_url != null) return false;
        if (groupId != null ? !groupId.equals(that.groupId) : that.groupId != null) return false;
        if (artifactID != null ? !artifactID.equals(that.artifactID) : that.artifactID != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;
        return fileName != null ? fileName.equals(that.fileName) : that.fileName == null;

    }

    @Override
    public int hashCode() {
        int result = guvnor_url != null ? guvnor_url.hashCode() : 0;
        result = 31 * result + (groupId != null ? groupId.hashCode() : 0);
        result = 31 * result + (artifactID != null ? artifactID.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DroolsResource{");
        sb.append("id=").append(id);
        sb.append(", guvnor_url='").append(guvnor_url).append('\'');
        sb.append(", groupId='").append(groupId).append('\'');
        sb.append(", artifactID='").append(artifactID).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", fileContent='").append(fileContent).append('\'');
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", startEventID=").append(startEventID);
        sb.append(", stopEventID=").append(stopEventID);
        sb.append('}');
        return sb.toString();
    }
}
