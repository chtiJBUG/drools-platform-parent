package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class MainFibonacciBPMN {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase = (DroolsPlatformKnowledgeBase) context.getBean("platformRunner");
        File ruleResource = null;
        RuleBasePackage ruleBasePackage=null;

        try {
            //ruleResource = ResourceUtils.getFile("classpath:fibonacci.drl");
            ruleBasePackage = droolsPlatformKnowledgeBase.getRuleBasePackage("ruleflow2.drl","RuleFlowProcess2.bpmn2");
            for (int i=0;i<100;i++) {
                RuleBaseSession ruleBaseSession = ruleBasePackage.createRuleBaseSession();
                Fibonacci fibonacci = new Fibonacci(3);
                ruleBaseSession.insertObject(fibonacci);
                ruleBaseSession.startProcess("P1");
                ruleBaseSession.fireAllRules();
                Thread.sleep(10000);
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            droolsPlatformKnowledgeBase.shutdown();

        }
        System.out.println("Hello World");
    }
}