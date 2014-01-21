package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/01/14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main (String[] args) throws DroolsChtijbugException {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
        DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase = (DroolsPlatformKnowledgeBase)context.getBean("platformRunner") ;
        RuleBasePackage ruleBasePackage = droolsPlatformKnowledgeBase.getRuleBasePackage("finonacci.drl");
        RuleBaseSession ruleBaseSession = ruleBasePackage.createRuleBaseSession();
        Fibonacci fibonacci = new Fibonacci(3);
        ruleBaseSession.insertObject(fibonacci);
        ruleBaseSession.fireAllRules();
       System.out.println("Hello World");
      }
}
