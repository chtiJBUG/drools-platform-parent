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
package org.chtijbug.drools.platform.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * Date: 05/05/14
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformResourceFile implements Serializable {
    private String fileName;
    private String content;
    private String guvnor_url = null;

    private String groupId = "default";
    private String artifactId="default";
    private String version;
    private String guvnor_userName;
    private String guvnor_password;

    public PlatformResourceFile() {
    }

    public PlatformResourceFile(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public PlatformResourceFile(String guvnor_url,String groupId,String artifactId,String version, String guvnor_userName, String guvnor_password) {
        this.guvnor_url = guvnor_url;


        this.groupId=groupId;
        this.artifactId=artifactId;
        this.version=version;

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



    public String getGuvnor_userName() {
        return guvnor_userName;
    }

    public String getGuvnor_password() {
        return guvnor_password;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGuvnor_url(String guvnor_url) {
        this.guvnor_url = guvnor_url;
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

    public void setGuvnor_userName(String guvnor_userName) {
        this.guvnor_userName = guvnor_userName;
    }

    public void setGuvnor_password(String guvnor_password) {
        this.guvnor_password = guvnor_password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformResourceFile{");
        sb.append("fileName='").append(fileName).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", guvnor_url='").append(guvnor_url).append('\'');

        sb.append(", groupId='").append(groupId).append('\'');
        sb.append(", artifactId='").append(artifactId).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", guvnor_userName='").append(guvnor_userName).append('\'');
        sb.append(", guvnor_password='").append(guvnor_password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
