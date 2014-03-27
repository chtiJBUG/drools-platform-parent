package org.chtijbug.drools.platform.rules;

import org.chtijbug.drools.entity.DroolsRuleObject;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFiredHistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFlowActivatedHistoryEvent;
import org.chtijbug.drools.entity.history.rule.AfterRuleFlowDeactivatedHistoryEvent;
import org.chtijbug.drools.platform.core.websocket.WebSocketServer;
import org.chtijbug.drools.platform.entity.PlatformRuntimeStatus;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsRessource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntime;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseBuilder;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/spring-test-persistence-config.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class PlatformRunTimeBeanTest {

    @Autowired
    private WebSocketServer webSocketServer;




    @Autowired
    HistoryListener historyListener;

    @Autowired
    PlatformRuntimeRepository platformRuntimeRepository;

    @Before
    public void setup() throws UnknownHostException {
        webSocketServer.run();
    }

    @Test
    public void testKnowledgeBaseCreate() throws DroolsChtijbugException {

        final List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();
        RuleBasePackage ruleBasePackage = RuleBaseBuilder.createPackageBasePackageWithListener(historyListener, "ruleflow2.drl", "RuleFlowProcess2.bpmn2");
        List<PlatformRuntime> platform1 = platformRuntimeRepository.findByHostnameAndEndDateNull("localhost");
        Assert.assertTrue(platform1.size() == 1);
        PlatformRuntime platforRuntime = platform1.get(0);
        Assert.assertTrue(platforRuntime.getEndDate() == null);
        Assert.assertTrue(platforRuntime.getStatus() == PlatformRuntimeStatus.STARTED);
        Assert.assertTrue(platforRuntime.getRuleBaseID() == 1l);
        List<DroolsRessource> listResources = platforRuntime.getDroolsRessources();
        Assert.assertTrue(listResources.size() == 2);
        DroolsRessource resource1 = listResources.get(0);
        Assert.assertTrue(resource1.getId() != null);
        Assert.assertTrue(resource1.getFileName().equals("ruleflow2.drl"));
        DroolsRessource resource2 = listResources.get(1);
        Assert.assertTrue(resource2.getId() != null);
        Assert.assertTrue(resource2.getFileName().equals("RuleFlowProcess2.bpmn2"));
        ruleBasePackage.dispose();
  //      List<PlatformRuntime> platform2 = platformRuntimeRepository.findActiveByHostName("localhost");
  //      Assert.assertTrue(platform2.size() == 1);
  //      PlatformRuntime platforRuntime2 = platform2.get(0);
  //      Assert.assertTrue(platforRuntime2.getEndDate() != null);
    }


    //@Test
    public void testBPMN2WorkFlowGroup() throws DroolsChtijbugException {

        final List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();
        RuleBasePackage ruleBasePackage = RuleBaseBuilder.createPackageBasePackageWithListener(historyListener, "ruleflow2.drl", "RuleFlowProcess2.bpmn2");
        int rulePackageID = ruleBasePackage.getRuleBaseID();

        RuleBaseSession ruleBaseSession1 = ruleBasePackage.createRuleBaseSession();
        Fibonacci fibonacci = new Fibonacci(0);
        ruleBaseSession1.insertObject(fibonacci);
        ruleBaseSession1.startProcess("P1");
        ruleBaseSession1.fireAllRules();
        Assert.assertTrue(historyEvents.size() == 40);
        Assert.assertTrue(historyEvents.get(11) instanceof AfterRuleFlowActivatedHistoryEvent);
        AfterRuleFlowActivatedHistoryEvent afterRuleFlowActivatedHistoryEvent = (AfterRuleFlowActivatedHistoryEvent) historyEvents.get(11);
        Assert.assertEquals(afterRuleFlowActivatedHistoryEvent.getRuleBaseID(), rulePackageID);
        Assert.assertEquals(afterRuleFlowActivatedHistoryEvent.getEventID(), 8l);
        Assert.assertEquals(afterRuleFlowActivatedHistoryEvent.getSessionId(), 1l);
        Assert.assertEquals(afterRuleFlowActivatedHistoryEvent.getTypeEvent(), HistoryEvent.TypeEvent.RuleFlowGroup);
        Assert.assertEquals(afterRuleFlowActivatedHistoryEvent.getDroolsRuleFlowGroupObject().getName(), "Group1");

        Assert.assertTrue(historyEvents.get(20) instanceof AfterRuleFiredHistoryEvent);
        AfterRuleFiredHistoryEvent afterRuleFiredHistoryEvent = (AfterRuleFiredHistoryEvent) historyEvents.get(20);
        Assert.assertEquals(afterRuleFiredHistoryEvent.getRuleBaseID(), rulePackageID);
        Assert.assertEquals(afterRuleFiredHistoryEvent.getEventID(), 17l);
        Assert.assertEquals(afterRuleFiredHistoryEvent.getSessionId(), 1l);
        Assert.assertEquals(afterRuleFiredHistoryEvent.getTypeEvent(), HistoryEvent.TypeEvent.Rule);
        DroolsRuleObject droolsRuleObject2 = afterRuleFiredHistoryEvent.getRule();
        Assert.assertEquals(droolsRuleObject2.getRuleName(), "Account group1");
        Assert.assertEquals(droolsRuleObject2.getRulePackageName(), "org.chtijbug.drools.runtime.test");
        Assert.assertEquals(afterRuleFiredHistoryEvent.getRuleInstanceId(), 1l);


        Assert.assertTrue(historyEvents.get(21) instanceof AfterRuleFlowDeactivatedHistoryEvent);
        AfterRuleFlowDeactivatedHistoryEvent afterRuleFlowDeactivatedHistoryEvent = (AfterRuleFlowDeactivatedHistoryEvent) historyEvents.get(21);
        Assert.assertEquals(afterRuleFlowDeactivatedHistoryEvent.getRuleBaseID(), rulePackageID);
        Assert.assertEquals(afterRuleFlowDeactivatedHistoryEvent.getEventID(), 18l);
        Assert.assertEquals(afterRuleFlowDeactivatedHistoryEvent.getSessionId(), 1l);
        Assert.assertEquals(afterRuleFlowDeactivatedHistoryEvent.getTypeEvent(), HistoryEvent.TypeEvent.RuleFlowGroup);
        Assert.assertEquals(afterRuleFlowDeactivatedHistoryEvent.getDroolsRuleFlowGroupObject().getName(), "Group1");

    }

}
