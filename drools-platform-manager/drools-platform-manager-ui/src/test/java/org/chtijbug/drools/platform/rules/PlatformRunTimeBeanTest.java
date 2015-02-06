package org.chtijbug.drools.platform.rules;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.guvnor.rest.ChtijbugDroolsRestException;
import org.chtijbug.drools.guvnor.rest.GuvnorConnexionFailedException;
import org.chtijbug.drools.guvnor.rest.GuvnorRepositoryConnector;
import org.chtijbug.drools.guvnor.rest.dt.DecisionTable;
import org.chtijbug.drools.guvnor.rest.model.Asset;
import org.chtijbug.drools.platform.backend.service.persistence.DecisionTableAssetManagementService;
import org.chtijbug.drools.platform.persistence.DTRuleAssetRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.RuleAssetRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstanceStatus;
import org.chtijbug.drools.platform.persistence.pojo.RuleAsset;
import org.chtijbug.drools.platform.rules.config.RuntimeSiteTopology;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseBuilder;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.resource.FileKnowledgeResource;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/spring-test-persistence-config.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class PlatformRunTimeBeanTest {

    private static TestWebSocketServer testWebSocketServer;

    private FileKnowledgeResource ruleflow2File;

    private FileKnowledgeResource ruleFlowProcess2;

    @Autowired
    DTRuleAssetRepository dtRuleAssetRepository;

    @Autowired
    RuleAssetRepository ruleAssetRepository;

    @Autowired
    DirectAccessHistoryListener historyListener;

    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Autowired
    private RuntimeSiteTopology runtimeSiteTopology;

    @Autowired
    private DecisionTableAssetManagementService decisionTableAssetManagementService;

    @BeforeClass
    public static void BeforeClass() throws UnknownHostException {
        testWebSocketServer = new TestWebSocketServer();
        testWebSocketServer.run();
    }

    @AfterClass
    public static void AfterClass() throws UnknownHostException {
        testWebSocketServer.stop();
        testWebSocketServer = null;
    }

    @Before
    public void BeforeTest() {

        ruleflow2File = FileKnowledgeResource.createDRLClassPathResource("ruleflow2.drl");
        ruleFlowProcess2 = FileKnowledgeResource.createDRLClassPathResource("RuleFlowProcess2.bpmn2");
        historyListener.getHistoryEvents().clear();
    }

    @Ignore
    @Test
    public void testKnowledgeBaseCreate() throws DroolsChtijbugException {


        RuleBasePackage ruleBasePackage = RuleBaseBuilder.createRuleBasePackage(1L, historyListener, "com.pummasoftware.fibonacci", "Fibonacci", Arrays.asList(ruleflow2File, ruleFlowProcess2));
        List<PlatformRuntimeInstance> platform1 = null; //platformRuntimeInstanceRepository.findByHostnameAndEndDateNull("localhost");
        Assert.assertTrue(platform1.size() == 1);
        PlatformRuntimeInstance platforRuntime = platform1.get(0);
        Assert.assertTrue(platforRuntime.getEndDate() == null);
        Assert.assertTrue(platforRuntime.getStatus() == PlatformRuntimeInstanceStatus.STARTED);
        Assert.assertTrue(platforRuntime.getRuleBaseID() == 1l);
        List<DroolsResource> listResources = platforRuntime.getDroolsRessources();
        Assert.assertTrue(listResources.size() == 2);
        DroolsResource resource1 = listResources.get(0);
        Assert.assertTrue(resource1.getId() != null);
        Assert.assertTrue(resource1.getFileName().equals("ruleflow2.drl"));
        DroolsResource resource2 = listResources.get(1);
        Assert.assertTrue(resource2.getId() != null);
        Assert.assertTrue(resource2.getFileName().equals("RuleFlowProcess2.bpmn2"));
        ruleBasePackage.dispose();
        //      List<PlatformRuntime> platform2 = platformRuntimeRepository.findActiveByHostName("localhost");
        //      Assert.assertTrue(platform2.size() == 1);
        //      PlatformRuntime platforRuntime2 = platform2.get(0);
        //      Assert.assertTrue(platforRuntime2.getEndDate() != null);
    }


    @Test
    @Ignore
    public void testBPMN2WorkFlowGroup() throws DroolsChtijbugException {

        final List<HistoryEvent> historyEvents = historyListener.getHistoryEvents();
        RuleBasePackage ruleBasePackage = RuleBaseBuilder.createRuleBasePackage(1L, historyListener, "com.pummasoftware.fibonacci", "Fibonacci", Arrays.asList(ruleflow2File, ruleFlowProcess2));
        Long rulePackageID = ruleBasePackage.getRuleBaseID();

        RuleBaseSession ruleBaseSession1 = ruleBasePackage.createRuleBaseSession();
        Fibonacci fibonacci = new Fibonacci(0);
        ruleBaseSession1.insertObject(fibonacci);
        ruleBaseSession1.startProcess("P1");
        ruleBaseSession1.fireAllRules();

        Assert.assertTrue(historyEvents.size() == 41);
        System.out.println("nbEvents=" + historyEvents.size());
        for (HistoryEvent h : historyEvents) {
            System.out.println("event=" + h.toString());
        }

    }


    @Test
    @Ignore
    public void testDTSycnrhonization() throws GuvnorConnexionFailedException, ChtijbugDroolsRestException {
        try {
            String packageName = "alertesecurite";
            GuvnorRepositoryConnector guvnorRepositoryConnector = new GuvnorRepositoryConnector(runtimeSiteTopology.buildGuvnorConfiguration());
            DecisionTable decisionTableGuvnorFormat = guvnorRepositoryConnector.getGuidedDecisionTable(packageName, "Alerte_DepLivraison_Marque_ConnaissanceClient");
            Asset asset = new Asset();
            asset.setName("Alerte_DepLivraison_Marque_ConnaissanceClient");
            asset.setVersionNumber("12");
            RuleAsset ruleAsset = new RuleAsset();
            ruleAsset.setPackageName(packageName);
            ruleAsset.setAssetName(asset.getName());
            ruleAsset.setVersionNumber(new Integer("8"));
            ruleAssetRepository.save(ruleAsset);
            decisionTableAssetManagementService.SynchronizeInDBContent(packageName, asset, decisionTableGuvnorFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void pipo() {

    }

}
