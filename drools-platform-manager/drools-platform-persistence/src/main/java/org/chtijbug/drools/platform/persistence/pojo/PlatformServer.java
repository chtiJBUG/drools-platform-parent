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


@Entity
@Table(name = "platform_server")
@Cacheable(value = true)
public class PlatformServer {
    @Id
    @SequenceGenerator(name = "platformserver_id_seq", sequenceName = "platform_server_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "platformserver_id_seq")
    private Long id;

    private String guvnorUrl;

    private String guvnorAppName;

    private String adminUsername;

    private String adminPassword;

    public PlatformServer() {
    }

    public PlatformServer(String guvnorUrl, String guvnorAppName, String adminUsername, String adminPassword) {
        this.guvnorUrl = guvnorUrl;
        this.guvnorAppName = guvnorAppName;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuvnorUrl() {
        return guvnorUrl;
    }

    public void setGuvnorUrl(String guvnorUrl) {
        this.guvnorUrl = guvnorUrl;
    }

    public String getGuvnorAppName() {
        return guvnorAppName;
    }

    public void setGuvnorAppName(String guvnorAppName) {
        this.guvnorAppName = guvnorAppName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlatformServer{");
        sb.append("id=").append(id);
        sb.append(", guvnorUrl='").append(guvnorUrl).append('\'');
        sb.append(", guvnorAppName='").append(guvnorAppName).append('\'');
        sb.append(", adminUsername='").append(adminUsername).append('\'');
        sb.append(", adminPassword='").append(adminPassword).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
