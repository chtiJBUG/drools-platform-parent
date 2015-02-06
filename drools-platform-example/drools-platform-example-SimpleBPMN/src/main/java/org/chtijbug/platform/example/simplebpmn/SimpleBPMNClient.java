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
package org.chtijbug.platform.example.simplebpmn;

import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBaseRuntime;
import org.chtijbug.drools.platform.runtime.javase.DroolsPlatformKnowledgeBaseJavaSE;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.resource.FileKnowledgeResource;

import java.util.ArrayList;
import java.util.List;


public class SimpleBPMNClient {
    public static void main(String[] args) {

        DroolsPlatformKnowledgeBaseRuntime droolsPlatformKnowledgeBase = null;
        try {

            FileKnowledgeResource drlFile = FileKnowledgeResource.createDRLClassPathResource("ruleflow2.drl");
            FileKnowledgeResource bpmnFile = FileKnowledgeResource.createBPMN2ClassPathResource("RuleFlowProcess2.bpmn2");
            List<KnowledgeResource> droolsResources = new ArrayList<>();
            droolsResources.add(drlFile);
            droolsResources.add(bpmnFile);
            droolsPlatformKnowledgeBase = new DroolsPlatformKnowledgeBaseJavaSE(12L, "group","artifact","version",droolsResources, "localhost", 22500, "localhost");
            while (droolsPlatformKnowledgeBase.isReady() == false) {
                System.out.println("sleep");
                Thread.sleep(2000);
            }
            for (int i = 0; i < 1; i++) {
                RuleBaseSession ruleBaseSession = droolsPlatformKnowledgeBase.createRuleBaseSession();
                Fibonacci fibonacci = new Fibonacci(0);
                ruleBaseSession.fireAllRulesAndStartProcess(fibonacci, "P1");
                ruleBaseSession.dispose();
                System.out.println("i= " + i);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            droolsPlatformKnowledgeBase.dispose();

        }
        System.out.println("Hello World");
        System.exit(0);
    }
}
