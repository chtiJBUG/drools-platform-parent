package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.DroolsRuleObject;
import org.chtijbug.drools.entity.history.HistoryContainer;
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.chtijbug.drools.runtime.mbeans.StatefulSessionSupervision;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * Date: 24/03/14
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public class DroolsPlatformSession implements RuleBaseSession {

    private RuleBaseStatefulSession ruleBaseStatefulSession;

    private WebSocketServerInstance runtimeWebSocketServerService;

    public RuleBaseStatefulSession getRuleBaseStatefulSession() {
        return ruleBaseStatefulSession;
    }

    public void setRuleBaseStatefulSession(RuleBaseStatefulSession ruleBaseStatefulSession) {
        this.ruleBaseStatefulSession = ruleBaseStatefulSession;
    }

    public WebSocketServerInstance getRuntimeWebSocketServerService() {
        return runtimeWebSocketServerService;
    }

    public void setRuntimeWebSocketServerService(WebSocketServerInstance runtimeWebSocketServerService) {
        this.runtimeWebSocketServerService = runtimeWebSocketServerService;
    }

    @Override
    public void insertObject(Object newObject) {
        this.ruleBaseStatefulSession.insertObject(newObject);
    }

    @Override
    public void insertByReflection(Object newObject) throws DroolsChtijbugException {
        this.ruleBaseStatefulSession.insertByReflection(newObject);
    }

    @Override
    public void setGlobal(String identifier, Object value) {
        this.ruleBaseStatefulSession.setGlobal(identifier, value);
    }

    @Override
    public void updateObject(Object updatedObject) {
        this.ruleBaseStatefulSession.updateObject(updatedObject);
    }

    @Override
    public void retractObject(Object oldObject) {
        this.ruleBaseStatefulSession.retractObject(oldObject);
    }

    private void sentJMXInfo() throws DroolsChtijbugException {
        PlatformManagementKnowledgeBean platformManagementKnowledgeBean = new PlatformManagementKnowledgeBean();
        platformManagementKnowledgeBean.setRequestRuntimePlarform(RequestRuntimePlarform.jmxInfos);
        platformManagementKnowledgeBean.setAlive(true);
        platformManagementKnowledgeBean.setRequestStatus(RequestStatus.SUCCESS);
        JMXInfo jmxInfo = new JMXInfo();
        platformManagementKnowledgeBean.setJmxInfo(jmxInfo);
        StatefulSessionSupervision mbeanStatefulSessionSupervision = ruleBaseStatefulSession.getMbeanStatefulSessionSupervision();
        jmxInfo.setAverageTimeExecution(mbeanStatefulSessionSupervision.getAverageTimeExecution());
        jmxInfo.setMinTimeExecution(mbeanStatefulSessionSupervision.getMinTimeExecution());
        jmxInfo.setMaxTimeExecution(mbeanStatefulSessionSupervision.getMaxTimeExecution());
        jmxInfo.setTotalTimeExecution(mbeanStatefulSessionSupervision.getTotalTimeExecution());
        jmxInfo.setTotalNumberRulesExecuted(mbeanStatefulSessionSupervision.getTotalNumberRulesExecuted());
        jmxInfo.setTotalNumberRulesExecuted(mbeanStatefulSessionSupervision.getAverageRulesExecuted());
        jmxInfo.setAverageRulesExecuted(mbeanStatefulSessionSupervision.getAverageRulesExecuted());
        jmxInfo.setMinRulesExecuted(mbeanStatefulSessionSupervision.getMinRulesExecuted());
        jmxInfo.setMaxRulesExecuted(mbeanStatefulSessionSupervision.getMaxRulesExecuted());
        jmxInfo.setNumberFireAllRulesExecuted(mbeanStatefulSessionSupervision.getNumberFireAllRulesExecuted());
        jmxInfo.setAverageRuleThroughout(mbeanStatefulSessionSupervision.getAverageRuleThroughout());
        jmxInfo.setMinRuleThroughout(mbeanStatefulSessionSupervision.getMinRuleThroughout());
        jmxInfo.setMaxRuleThroughout(mbeanStatefulSessionSupervision.getMaxRuleThroughout());
        try {
            this.runtimeWebSocketServerService.sendMessage(platformManagementKnowledgeBean);
        } catch (Exception e) {

            DroolsChtijbugException ee = new DroolsChtijbugException("JMXInfo", "Not Possible to send Infos", e);
            throw ee;
        }
    }

    @Override
    public void fireAllRules() throws DroolsChtijbugException {
        this.ruleBaseStatefulSession.fireAllRules();
        this.sentJMXInfo();
    }

    @Override
    public Object fireAllRulesAndStartProcess(Object inputObject, String processName) throws DroolsChtijbugException {
        Object returnObject = this.ruleBaseStatefulSession.fireAllRulesAndStartProcess(inputObject, processName);
        this.sentJMXInfo();
        return returnObject;
    }

    @Override
    public void startProcess(String processName) {
        this.ruleBaseStatefulSession.startProcess(processName);
    }

    @Override
    public void dispose() {
        this.ruleBaseStatefulSession.dispose();
    }

    @Override
    public HistoryContainer getHistoryContainer() {
        return this.ruleBaseStatefulSession.getHistoryContainer();
    }

    @Override
    public String getHistoryContainerXML() {
        return this.ruleBaseStatefulSession.getHistoryContainerXML();
    }

    @Override
    public Collection<DroolsFactObject> listLastVersionObjects() {
        return this.ruleBaseStatefulSession.listLastVersionObjects();
    }

    @Override
    public String listLastVersionObjectsXML() {
        return this.ruleBaseStatefulSession.listLastVersionObjectsXML();
    }

    @Override
    public Collection<DroolsRuleObject> listRules() {
        return this.ruleBaseStatefulSession.listRules();
    }

    @Override
    public int getNumberRulesExecuted() {
        return this.ruleBaseStatefulSession.getNumberRulesExecuted();
    }

    @Override
    public int getSessionId() {
        return this.ruleBaseStatefulSession.getSessionId();
    }

    @Override
    public int getRuleBaseID() {
        return this.ruleBaseStatefulSession.getRuleBaseID();
    }
}
