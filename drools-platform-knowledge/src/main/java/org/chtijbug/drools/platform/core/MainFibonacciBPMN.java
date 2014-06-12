package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsResource;
import org.chtijbug.drools.runtime.resource.DrlDroolsResource;
import org.chtijbug.drools.runtime.resource.DroolsResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class MainFibonacciBPMN {
    public static void main(String[] args) {

         RuleBasePackage droolsPlatformKnowledgeBase =null;
        try {
            DrlDroolsResource drlFile = DrlDroolsResource.createClassPathResource("ruleflow2.drl");
            Bpmn2DroolsResource bpmnFile = Bpmn2DroolsResource.createClassPathResource("RuleFlowProcess2.bpmn2");;
            List<DroolsResource> droolsResources = new ArrayList<>();
            droolsResources.add(drlFile);
            droolsResources.add(bpmnFile);
            droolsPlatformKnowledgeBase = new DroolsPlatformKnowledgeBase(11,droolsResources,"localhost",22500 ,"localhost" );
             DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase1=(DroolsPlatformKnowledgeBase)droolsPlatformKnowledgeBase;
             while (droolsPlatformKnowledgeBase1.isReady()==false){
                 System.out.println("sleep");
                 Thread.sleep(2000);
             }
             for (int i=0;i<1;i++) {
                RuleBaseSession ruleBaseSession = droolsPlatformKnowledgeBase.createRuleBaseSession();
                Fibonacci fibonacci = new Fibonacci(0);
                ruleBaseSession.insertObject(fibonacci);
                ruleBaseSession.startProcess("P1");
                ruleBaseSession.fireAllRules();
                ruleBaseSession.dispose();
                 System.out.println("i= "+i);
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
