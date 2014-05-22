package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
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
public class MainFibonacciNoBPMN {
    public static void main(String[] args) {
         RuleBasePackage droolsPlatformKnowledgeBase = null;
        try {
            DrlDroolsRessource drlFile = DrlDroolsRessource.createClassPathResource("fibonacci.drl");
             List<DroolsResource> droolsResources = new ArrayList<>();
             droolsResources.add(drlFile);
             droolsPlatformKnowledgeBase = new DroolsPlatformKnowledgeBase(12,droolsResources,"localhost",22600 ,"localhost" );
            for (int i = 0; i < 2; i++) {
                RuleBaseSession ruleBaseSession = droolsPlatformKnowledgeBase.createRuleBaseSession();
                Fibonacci fibonacci = new Fibonacci(3);
                ruleBaseSession.insertObject(fibonacci);
                ruleBaseSession.fireAllRules();
                ruleBaseSession.dispose();
                Thread.sleep(10000);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            droolsPlatformKnowledgeBase.dispose();

        }
        System.out.println("Hello World");
    }
}
