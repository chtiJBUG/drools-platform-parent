package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.platform.core.droolslistener.JmsStorageHistoryListener;
import org.chtijbug.drools.platform.core.websocket.RuntimeWebSocketServerService;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.RuleBaseSingleton;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/12/13
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DroolsPlatformKnowledgeBase implements RuleBasePackage {
    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBase.class);


    private RuleBaseSingleton ruleBasePackage;
    @Autowired
    private JmsStorageHistoryListener jmsStorageHistoryListener;

    private RuntimeWebSocketServerService runtimeWebSocketServerService;
    @Resource
    private List<DroolsResource> droolsResources = new ArrayList<>();


    public void initFromGuvnor() throws DroolsChtijbugException {
        logger.debug(">>createGuvnorRuleBasePackage", this.toString());
        jmsStorageHistoryListener.setDroolsPlatformKnowledgeBase(this);
        ruleBasePackage = new RuleBaseSingleton(RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.jmsStorageHistoryListener);
        if (droolsResources.size() == 1 && droolsResources.get(0) instanceof GuvnorDroolsResource) {
            ruleBasePackage.createKBase(droolsResources.get(0));
        } else {
            throw new RuntimeException("List not correct");
        }
        jmsStorageHistoryListener.setMbsRuleBase(ruleBasePackage.getMbsRuleBase());
        jmsStorageHistoryListener.setMbsSession(ruleBasePackage.getMbsSession());

        this.ruleBasePackage = ruleBasePackage;
        logger.debug("<<createGuvnorRuleBasePackage", ruleBasePackage);
    }

    public void initFromFiles() throws DroolsChtijbugException {
        logger.debug(">>createPackageBasePackage");

        jmsStorageHistoryListener.setDroolsPlatformKnowledgeBase(this);
        ruleBasePackage = new RuleBaseSingleton(RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.jmsStorageHistoryListener);
        try {
            ruleBasePackage.createKBase(droolsResources);
        } finally {
            logger.debug("<<createPackageBasePackage", ruleBasePackage);
        }
    }


    public void shutdown() {
        this.jmsStorageHistoryListener.shutdown();
        this.jmsStorageHistoryListener = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.jmsStorageHistoryListener = null;
    }

    private String getFileExtension(String ressourceName) {
        int mid = ressourceName.lastIndexOf(".");
        String ext = ressourceName.substring(mid + 1, ressourceName.length()).toUpperCase();
        return ext;
    }





    public void setRuntimeWebSocketServerService(RuntimeWebSocketServerService runtimeWebSocketServerService) {
        this.runtimeWebSocketServerService = runtimeWebSocketServerService;
    }


    @Override
    public RuleBaseSession createRuleBaseSession() throws DroolsChtijbugException {
        RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession();
        DroolsPlatformSession droolsPlatformSession = new DroolsPlatformSession();
        droolsPlatformSession.setRuntimeWebSocketServerService(this.runtimeWebSocketServerService);
        droolsPlatformSession.setRuleBaseStatefulSession(created);
        return droolsPlatformSession;
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {
        RuleBaseStatefulSession created = (RuleBaseStatefulSession) this.ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute);
        DroolsPlatformSession droolsPlatformSession = new DroolsPlatformSession();
        droolsPlatformSession.setRuntimeWebSocketServerService(this.runtimeWebSocketServerService);
        droolsPlatformSession.setRuleBaseStatefulSession(created);
        return droolsPlatformSession;
    }

    @Override
    public void createKBase(DroolsResource... res) throws DroolsChtijbugException {
        this.ruleBasePackage.createKBase(res);
    }

    @Override
    public void createKBase(List<DroolsResource> res) throws DroolsChtijbugException {
        this.ruleBasePackage.createKBase(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(DroolsResource... res) throws DroolsChtijbugException {
        this.ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(List<DroolsResource> res) throws DroolsChtijbugException {
        this.ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void ReloadWithSameRessources() throws DroolsChtijbugException {
        this.ruleBasePackage.ReloadWithSameRessources();
    }

    @Override
    public HistoryListener getHistoryListener() {
        return this.ruleBasePackage.getHistoryListener();
    }

    @Override
    public int getRuleBaseID() {
        return this.ruleBasePackage.getRuleBaseID();
    }

    @Override
    public void dispose() {
        this.ruleBasePackage.dispose();
        this.shutdown();
    }

    @Override
    public void cleanup() {
        this.ruleBasePackage.cleanup();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DroolsPlatformKnowledgeBase{");

        sb.append(", ruleBasePackage=").append(ruleBasePackage);
        sb.append('}');
        return sb.toString();
    }
}
