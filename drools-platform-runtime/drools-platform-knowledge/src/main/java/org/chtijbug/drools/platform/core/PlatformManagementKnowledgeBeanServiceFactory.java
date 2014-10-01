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
package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.platform.entity.Heartbeat;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.PlatformResourceFile;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsResource;
import org.chtijbug.drools.runtime.resource.DrlDroolsResource;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PlatformManagementKnowledgeBeanServiceFactory {

    public static PlatformManagementKnowledgeBean isAlive(PlatformManagementKnowledgeBean bean) {
        bean.setAlive(true);
        bean.setRequestStatus(RequestStatus.SUCCESS);
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setLastAlive(new Date());
        bean.setHeartbeat(heartbeat);
        return bean;
    }

    public static PlatformManagementKnowledgeBean generateRuleVersionsInfo(PlatformManagementKnowledgeBean bean, List<DroolsResource> droolsResources) {
        for (DroolsResource droolsResource : droolsResources) {
            if (droolsResource instanceof GuvnorDroolsResource) {
                GuvnorDroolsResource guvnorDroolsResource = (GuvnorDroolsResource) droolsResource;
                PlatformResourceFile platformResourceFile = new PlatformResourceFile(guvnorDroolsResource.getBaseUrl(), guvnorDroolsResource.getWebappName(), guvnorDroolsResource.getPackageName(), guvnorDroolsResource.getPackageVersion(), null, null);
                bean.getResourceFileList().add(platformResourceFile);
            } else if (droolsResource instanceof DrlDroolsResource) {
                DrlDroolsResource drlDroolsResource = (DrlDroolsResource) droolsResource;
                PlatformResourceFile platformResourceFile = new PlatformResourceFile(drlDroolsResource.getFileName(), drlDroolsResource.getFileContent());
                bean.getResourceFileList().add(platformResourceFile);
            }
        }
        bean.setRequestStatus(RequestStatus.SUCCESS);
        return bean;
    }

    public static List<DroolsResource> extract(List<PlatformResourceFile> resourceFiles, String guvnorUsername, String guvnorPassword) {
        List<DroolsResource> droolsResources = new ArrayList<>();
        for (PlatformResourceFile resourceFile : resourceFiles) {
            if (!"empty".equals(resourceFile.getGuvnor_url())) {
                DroolsResource droolsResource = GuvnorDroolsResource.createGuvnorRessource(resourceFile.getGuvnor_url(), resourceFile.getGuvnor_appName(), resourceFile.getGuvnor_packageName(), resourceFile.getGuvnor_packageVersion(), guvnorUsername, guvnorPassword);
                droolsResources.add(droolsResource);
            } else {
                String extension = getFileExtension(resourceFile.getFileName());
                if ("bpmn2".equals(extension)) {
                    Bpmn2DroolsResource bpmn2DroolsResource = Bpmn2DroolsResource.createClassPathResource(resourceFile.getFileName());
                    droolsResources.add(bpmn2DroolsResource);
                } else if ("drl".equals(extension)) {
                    DroolsResource droolsResource = DrlDroolsResource.createClassPathResource(resourceFile.getFileName());
                    droolsResources.add(droolsResource);
                }
            }
        }
        return droolsResources;
    }

    private static String getFileExtension(String fileName) {
        String extension = null;
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}
