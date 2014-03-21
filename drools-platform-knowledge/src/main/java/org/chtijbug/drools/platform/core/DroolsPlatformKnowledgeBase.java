package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.platform.core.droolslistener.JmsStorageHistoryListener;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.impl.RuleBaseSingleton;
import org.chtijbug.drools.runtime.resource.Bpmn2DroolsRessource;
import org.chtijbug.drools.runtime.resource.DrlDroolsRessource;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.chtijbug.drools.runtime.resource.GuvnorDroolsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/12/13
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public class DroolsPlatformKnowledgeBase {
    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBase.class);
    @Value("${guvnor.url}")
    private String guvnor_url;
    @Value("${guvnor.appName}")
    private String guvnor_appName;
    @Value("${guvnor.packageName}")
    private String guvnor_packageName;
    @Value("${guvnor.packageVersion}")
    private String guvnor_packageVersion;
    @Value("${guvnor.username}")
    private String guvnor_username;
    @Value("${guvnor.password}")
    private String guvnor_password;

    private RuleBasePackage ruleBasePackage;
    @Autowired
    private  JmsStorageHistoryListener jmsStorageHistoryListener;

    public void getGuvnorRuleBasePackage() throws DroolsChtijbugException {

        logger.debug(">>createGuvnorRuleBasePackage", this.toString());
        RuleBaseSingleton newRuleBasePackage = new RuleBaseSingleton(RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.jmsStorageHistoryListener);
        GuvnorDroolsResource gdr = new GuvnorDroolsResource(guvnor_url, guvnor_appName, guvnor_packageName, guvnor_packageVersion, guvnor_username, guvnor_password);
        newRuleBasePackage.createKBase(gdr);
        jmsStorageHistoryListener.setMbsRuleBase(newRuleBasePackage.getMbsRuleBase());
        jmsStorageHistoryListener.setMbsSession(newRuleBasePackage.getMbsSession());

        this.ruleBasePackage = newRuleBasePackage;
        logger.debug("<<createGuvnorRuleBasePackage", newRuleBasePackage);
    }

    public RuleBasePackage getRuleBasePackage(String... filenames) throws DroolsChtijbugException {
        logger.debug(">>createPackageBasePackage");
        RuleBasePackage ruleBasePackage = new RuleBaseSingleton(RuleBaseSingleton.DEFAULT_RULE_THRESHOLD, this.jmsStorageHistoryListener);
        try {
            List<DroolsResource> droolsResources = new ArrayList<DroolsResource>();
            for (String filename : filenames) {
                String extensionName = this.getFileExtension(filename);
                DroolsResource resource = null;
                if ("DRL".equals(extensionName)) {
                    resource = DrlDroolsRessource.createClassPathResource(filename);
                } else if ("BPMN2".equals(extensionName)) {
                    resource = Bpmn2DroolsRessource.createClassPathResource(filename);
                }
                if (resource != null) {
                    ruleBasePackage.createKBase(resource);
                } else {
                    throw new DroolsChtijbugException(DroolsChtijbugException.UnknowFileExtension, filename, null);
                }
                droolsResources.add(resource);
            }
            ruleBasePackage.createKBase(droolsResources);
            this.ruleBasePackage = ruleBasePackage;
            //_____ Returning the result
            return ruleBasePackage;
        } finally {
            logger.debug("<<createPackageBasePackage", ruleBasePackage);
        }
    }

    public void shutdown(){
       this.jmsStorageHistoryListener.shutdown();
       this.jmsStorageHistoryListener= null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.jmsStorageHistoryListener= null;
    }

    private String getFileExtension(String ressourceName) {
        int mid = ressourceName.lastIndexOf(".");
        String ext = ressourceName.substring(mid + 1, ressourceName.length()).toUpperCase();
        return ext;
    }

    public String getGuvnor_url() {

        return guvnor_url;
    }

    public void setGuvnor_url(String guvnor_url) {
        this.guvnor_url = guvnor_url;
    }

    public String getGuvnor_appName() {
        return guvnor_appName;
    }

    public void setGuvnor_appName(String guvnor_appName) {
        this.guvnor_appName = guvnor_appName;
    }

    public String getGuvnor_packageName() {
        return guvnor_packageName;
    }

    public void setGuvnor_packageName(String guvnor_packageName) {
        this.guvnor_packageName = guvnor_packageName;
    }

    public String getGuvnor_packageVersion() {
        return guvnor_packageVersion;
    }

    public void setGuvnor_packageVersion(String guvnor_packageVersion) {
        this.guvnor_packageVersion = guvnor_packageVersion;
    }

    public String getGuvnor_username() {
        return guvnor_username;
    }

    public void setGuvnor_username(String guvnor_username) {
        this.guvnor_username = guvnor_username;
    }

    public String getGuvnor_password() {
        return guvnor_password;
    }

    public void setGuvnor_password(String guvnor_password) {
        this.guvnor_password = guvnor_password;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DroolsPlatformKnowledgeBase{");
        sb.append("guvnor_url='").append(guvnor_url).append('\'');
        sb.append(", guvnor_appName='").append(guvnor_appName).append('\'');
        sb.append(", guvnor_packageName='").append(guvnor_packageName).append('\'');
        sb.append(", guvnor_packageVersion='").append(guvnor_packageVersion).append('\'');
        sb.append(", guvnor_username='").append(guvnor_username).append('\'');
        sb.append(", guvnor_password='").append(guvnor_password).append('\'');
        sb.append(", ruleBasePackage=").append(ruleBasePackage);
        sb.append('}');
        return sb.toString();
    }
}
