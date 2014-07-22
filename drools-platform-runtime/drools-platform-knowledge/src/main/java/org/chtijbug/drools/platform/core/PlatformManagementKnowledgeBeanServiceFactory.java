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

/**
 * Created by IntelliJ IDEA.
 * Date: 16/06/14
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
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
