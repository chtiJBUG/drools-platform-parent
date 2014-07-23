package org.chtijbug.drools.platform.runtime.servlet;

import com.google.common.base.Throwables;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBaseRuntime;
import org.chtijbug.drools.platform.runtime.servlet.historylistener.ServletJmsStorageHistoryListener;
import org.chtijbug.drools.platform.runtime.servlet.websocket.SpringWebSocketServer;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.JavaDialect;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.chtijbug.drools.runtime.resource.DroolsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/06/14
 * Time: 12:59
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DroolsPlatformKnowledgeBaseJavaEE implements DroolsPlatformKnowledgeBaseRuntime {

    /**
     * Class Logger
     */
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformKnowledgeBaseJavaEE.class);
    /**
     * Rule base ID (UID for the runtime
     */
    private Integer ruleBaseID;
    /**
     * Rule base singleton (Knowledge session factory)
     */
    private DroolsPlatformKnowledgeBaseRuntime ruleBasePackage;


    private Semaphore startAndGo = new Semaphore(1);

    /** */


    private ServletJmsStorageHistoryListener servletJmsStorageHistoryListener;
    /** */
    private List<DroolsResource> droolsResources = new ArrayList<>();
    /**
     * Instant messaging channel *
     */
    private String webSocketHostname;


    private SpringWebSocketServer webSocketServer;

    private String webSocketEndPoint;

    private int webSocketPort = 8080;
    /**
     * Event Messaging channel settings
     */
    private String platformServer;

    private Integer platformPort = 61616;

    private String platformQueueName = "historyEventQueue";

    private JavaDialect javaDialect = null;

    public DroolsPlatformKnowledgeBaseJavaEE() {
    }

    public DroolsPlatformKnowledgeBaseJavaEE(Integer ruleBaseID, List<DroolsResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer, JavaDialect javaDialect) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.platformServer = platformServer;
        this.javaDialect = javaDialect;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaEE(Integer ruleBaseID, List<DroolsResource> droolsResources,
                                             String webSocketHostname, int webSocketPort,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.webSocketPort = webSocketPort;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public DroolsPlatformKnowledgeBaseJavaEE(Integer ruleBaseID, List<DroolsResource> droolsResources,
                                             String webSocketHostname,
                                             String platformServer) {
        this.ruleBaseID = ruleBaseID;
        this.droolsResources = droolsResources;
        this.webSocketHostname = webSocketHostname;
        this.platformServer = platformServer;
        initPlatformRuntime();
    }

    public void initPlatformRuntime() {
        logger.debug(">>createPackageBasePackage");
        logger.debug("<<<createPackageBasePackage");
    }

    public void startConnectionToPlatform() {
        try {
            startAndGo.acquire();
            if (this.webSocketServer != null && this.servletJmsStorageHistoryListener != null) {
                ruleBasePackage = new DroolsPlatformKnowledgeBase(this.ruleBaseID, this.droolsResources, this.javaDialect, this.webSocketServer, this.servletJmsStorageHistoryListener);
            }
            startAndGo.release();
        } catch (DroolsChtijbugException | InterruptedException | UnknownHostException e) {
            logger.error("Error while initialisazing caused by {}", e);
            throw Throwables.propagate(e);
        } finally {
            logger.debug("<<<createPackageBasePackage");

        }
    }

    public void setServletJmsStorageHistoryListener(ServletJmsStorageHistoryListener servletJmsStorageHistoryListener) {
        try {
            startAndGo.acquire();
            this.servletJmsStorageHistoryListener = servletJmsStorageHistoryListener;
            startAndGo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void setWebSocketServer(SpringWebSocketServer webSocketServer) {
        try {
            startAndGo.acquire();
            this.webSocketServer = webSocketServer;
            startAndGo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public RuleBaseSession createRuleBaseSession() throws DroolsChtijbugException {
        return ruleBasePackage.createRuleBaseSession();
    }

    @Override
    public RuleBaseSession createRuleBaseSession(int maxNumberRulesToExecute) throws DroolsChtijbugException {
        return ruleBasePackage.createRuleBaseSession(maxNumberRulesToExecute);
    }

    @Override
    public void createKBase(DroolsResource... res) throws DroolsChtijbugException {
        ruleBasePackage.createKBase(res);
    }

    @Override
    public void createKBase(List<DroolsResource> res) throws DroolsChtijbugException {
        ruleBasePackage.createKBase(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(DroolsResource... res) throws DroolsChtijbugException {
        ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void RecreateKBaseWithNewRessources(List<DroolsResource> res) throws DroolsChtijbugException {
        ruleBasePackage.RecreateKBaseWithNewRessources(res);
    }

    @Override
    public void ReloadWithSameRessources() throws DroolsChtijbugException {
        ruleBasePackage.ReloadWithSameRessources();
    }

    @Override
    public HistoryListener getHistoryListener() {
        return ruleBasePackage.getHistoryListener();
    }

    @Override
    public int getRuleBaseID() {
        return this.ruleBaseID;
    }

    @Override
    public void dispose() {
        ruleBasePackage.dispose();
    }

    @Override
    public void cleanup() {
        ruleBasePackage.dispose();
    }

    @Override
    public boolean isReady() {
        return this.ruleBasePackage.isReady();
    }


    @Override
    public List<DroolsResource> getDroolsResources() {
        return this.droolsResources;
    }

    @Override
    public void setRuleBaseStatus(boolean b) {
        ruleBasePackage.setRuleBaseStatus(b);
    }

    @Override
    public String getGuvnorUsername() {
        return ruleBasePackage.getGuvnorUsername();
    }


    @Override
    public String getGuvnorPassword() {
        return ruleBasePackage.getGuvnorPassword();
    }

    public void setRuleBaseID(Integer ruleBaseID) {
        this.ruleBaseID = ruleBaseID;
    }

    public void setWebSocketHostname(String webSocketHostname) {
        this.webSocketHostname = webSocketHostname;
    }

    public void setWebSocketPort(int webSocketPort) {
        this.webSocketPort = webSocketPort;
    }

    public void setEndPoint(String endPoint) {
        this.webSocketEndPoint = endPoint;
    }

    public void setPlatformServer(String platformServer) {
        this.platformServer = platformServer;
    }

    public void setPlatformPort(Integer platformPort) {
        this.platformPort = platformPort;
    }

    public void setDroolsResources(List<DroolsResource> droolsResources) {
        this.droolsResources = droolsResources;
    }

    public String getWebSocketHostname() {
        return webSocketHostname;
    }

    public int getWebSocketPort() {
        return webSocketPort;
    }

    public String getWebSocketEndPoint() {
        return webSocketEndPoint;
    }

    public String getPlatformServer() {
        return platformServer;
    }
}
