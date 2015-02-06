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
package org.chtijbug.drools.platform.rules.config;

import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.chtijbug.drools.guvnor.GuvnorConnexionConfiguration;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import static com.google.common.base.Throwables.propagate;
import static java.util.Arrays.asList;

@Component
public class RuntimeSiteTopology {
    /**
     * Default values
     */
    private String guvnorUrl = "http://localhost:8080";
    private String guvnorApplicationName = "drools-guvnor";
    private String organizationalUnitName;

    private String repositoryName;
    private String guvnorPackageName = "default";
    private String guvnorUserName = "admin";
    private String guvnorPassword = "admin";

    private Properties allEnvironments = new Properties();

    private Map<String, Environment> environments = new TreeMap<>();

    public WebClient webClient(String envName, String resourcePath) {
        try {
            URI uri = environments.get(envName).getUrl().toURI();
            URI baseUri = uri.resolve("/");
            JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
            WebClient client = WebClient.create(baseUri.toString(), asList(jsonProvider))
                    .path(uri.getPath().concat(resourcePath));
            //_____ Adding no timeout feature for long running process
            ClientConfiguration config = WebClient.getConfig(client);
            HTTPConduit http = (HTTPConduit) config.getConduit();
            HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
            /* connection timeout for requesting the rule package binaries */
            httpClientPolicy.setConnectionTimeout(0L);
            /* Reception timeout */
            httpClientPolicy.setReceiveTimeout(0L);
            http.setClient(httpClientPolicy);
            return client;
        } catch (URISyntaxException e) {
            throw propagate(e);
        }
    }

    public Collection<Environment> getEnvironments() {
        return environments.values();
    }

    @PostConstruct
    public void init() throws Exception {
        for (Map.Entry entry : allEnvironments.entrySet()) {
            String name = (String) entry.getKey();
            URL value = new URL((String) entry.getValue());
            environments.put(name, new Environment(name, value));
        }
    }

    public String getGuvnorUrl() {
        return guvnorUrl;
    }

    public void setGuvnorUrl(String guvnorUrl) {
        this.guvnorUrl = guvnorUrl;
    }

    public String getGuvnorApplicationName() {
        return guvnorApplicationName;
    }

    public void setGuvnorApplicationName(String guvnorApplicationName) {
        this.guvnorApplicationName = guvnorApplicationName;
    }

    public String getGuvnorPackageName() {
        return guvnorPackageName;
    }

    public void setGuvnorPackageName(String guvnorPackageName) {
        this.guvnorPackageName = guvnorPackageName;
    }

    public String getGuvnorUserName() {
        return guvnorUserName;
    }

    public void setGuvnorUserName(String guvnorUserName) {
        this.guvnorUserName = guvnorUserName;
    }

    public String getGuvnorPassword() {
        return guvnorPassword;
    }

    public void setGuvnorPassword(String guvnorPassword) {
        this.guvnorPassword = guvnorPassword;
    }

    public void setAllEnvironments(Properties allEnvironments) {
        this.allEnvironments = allEnvironments;
    }

    public Properties getAllEnvironments() {
        return allEnvironments;
    }

    public String getOrganizationalUnitName() {
        return organizationalUnitName;
    }

    public void setOrganizationalUnitName(String organizationalUnitName) {
        this.organizationalUnitName = organizationalUnitName;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public GuvnorConnexionConfiguration buildGuvnorConfiguration() {
        return new GuvnorConnexionConfiguration(
                getGuvnorUrl(),
                getGuvnorApplicationName(),
                getOrganizationalUnitName(),
                getRepositoryName(),
                getGuvnorPackageName(),
                getGuvnorUserName(),
                getGuvnorPassword());

    }

}
