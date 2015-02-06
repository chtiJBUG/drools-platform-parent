package org.chtijbug.drools.platform.runtime.javase;


import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.runtime.resource.WorkbenchKnowledgeResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 02/12/14
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String args[]) throws Exception {


        WorkbenchKnowledgeResource guvnorRessource = new WorkbenchKnowledgeResource("http://localhost:8080", "drools-guvnor",
                        "loyalty", "LATEST");

        List<KnowledgeResource> droolsResources = new ArrayList<>();

        droolsResources.add(guvnorRessource);
        DroolsPlatformKnowledgeBaseJavaSE ruleBasePackage = new DroolsPlatformKnowledgeBaseJavaSE();
        ruleBasePackage.setRuleBaseID(5500L);
        ruleBasePackage.setWebSocketHostname("192.168.2.11");
        ruleBasePackage.setPlatformServer("192.168.2.11");
        ruleBasePackage.setDroolsResources(droolsResources);
        ruleBasePackage.initPlatformRuntime();
        int i = 0;
        while (ruleBasePackage.isReady() == false && i < 120) {
            Thread.sleep(1000);
            i++;
        }

    }
}
