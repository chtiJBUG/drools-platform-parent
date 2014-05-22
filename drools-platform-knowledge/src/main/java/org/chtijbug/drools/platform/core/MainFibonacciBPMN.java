package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsRessource;
import org.chtijbug.drools.runtime.resource.DrlDroolsRessource;
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
            DrlDroolsRessource drlFile = DrlDroolsRessource.createClassPathResource("ruleflow2.drl");
            Bpmn2DroolsRessource bpmnFile = Bpmn2DroolsRessource.createClassPathResource("RuleFlowProcess2.bpmn2");;
            List<DroolsResource> droolsResources = new ArrayList<>();
            droolsResources.add(drlFile);
            droolsResources.add(bpmnFile);
            droolsPlatformKnowledgeBase = new DroolsPlatformKnowledgeBase(11,droolsResources,"localhost",22500 ,"localhost" );
             DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase1=(DroolsPlatformKnowledgeBase)droolsPlatformKnowledgeBase;
             while (droolsPlatformKnowledgeBase1.isReady()==false){
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
    }
}
