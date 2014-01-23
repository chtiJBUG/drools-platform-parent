package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ResourceUtils;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase = (DroolsPlatformKnowledgeBase) context.getBean("platformRunner");
        File ruleResource = null;
        RuleBasePackage ruleBasePackage=null;

        try {
            ruleResource = ResourceUtils.getFile("classpath:fibonacci.drl");
            ruleBasePackage = droolsPlatformKnowledgeBase.getRuleBasePackage(ruleResource.getAbsolutePath());
            RuleBaseSession ruleBaseSession = ruleBasePackage.createRuleBaseSession();
            Fibonacci fibonacci = new Fibonacci(3);
            ruleBaseSession.insertObject(fibonacci);
            ruleBaseSession.fireAllRules();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            droolsPlatformKnowledgeBase.shutdown();

        }
        System.out.println("Hello World");
    }
}
