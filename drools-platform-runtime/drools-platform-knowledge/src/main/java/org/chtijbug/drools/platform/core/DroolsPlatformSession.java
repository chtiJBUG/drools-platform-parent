/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.DroolsRuleObject;
import org.chtijbug.drools.entity.history.HistoryContainer;
import org.chtijbug.drools.platform.core.wssocket.WebSocketClient;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.chtijbug.drools.runtime.mbeans.StatefulSessionSupervision;
import org.drools.ObjectFilter;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItemHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;


public class DroolsPlatformSession implements RuleBaseSession {


    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformSession.class);
    private RuleBaseStatefulSession ruleBaseStatefulSession;

    private WebSocketClient webSocketClient;

    public DroolsPlatformSession(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public RuleBaseStatefulSession getRuleBaseStatefulSession() {
        return ruleBaseStatefulSession;
    }

    public void setRuleBaseStatefulSession(RuleBaseStatefulSession ruleBaseStatefulSession) {
        this.ruleBaseStatefulSession = ruleBaseStatefulSession;
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
        platformManagementKnowledgeBean.setRuleBaseId(this.getRuleBaseID());
        platformManagementKnowledgeBean.setRequestStatus(RequestStatus.SUCCESS);
        JMXInfo jmxInfo = new JMXInfo();
        platformManagementKnowledgeBean.setJmxInfo(jmxInfo);
        StatefulSessionSupervision mbeanStatefulSessionSupervision = ruleBaseStatefulSession.getMbeanStatefulSessionSupervision();
        if (mbeanStatefulSessionSupervision!= null) {
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
        }
        try {
            this.webSocketClient.sendMessage(platformManagementKnowledgeBean);
        } catch (Exception e) {
            logger.error("DroolsPlatformSession.sentJMXInfo", e);

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
    public Object fireAllRulesAndStartProcessWithParam(Object inputObject, String processName) throws DroolsChtijbugException {
        Object returnObject = this.ruleBaseStatefulSession.fireAllRulesAndStartProcessWithParam(inputObject, processName);
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

    @Override
    public Collection<Object> getObjects(ObjectFilter objectFilter) {
        return this.ruleBaseStatefulSession.getObjects(objectFilter);
    }

    @Override
    public void completeWorkItem(long processId, Map<String, Object> vars) {
        this.ruleBaseStatefulSession.completeWorkItem(processId,vars);
    }

    @Override
    public void abortWorkItem(long processId) {
        this.ruleBaseStatefulSession.abortWorkItem(processId);
    }

    @Override
    public void registerWorkItemHandler(String workItemName, WorkItemHandler workItemHandler) {
        this.ruleBaseStatefulSession.registerWorkItemHandler(workItemName,workItemHandler);
    }

    @Override
    public ProcessInstance startProcess(String processName, Map<String, Object> vars) {
        return this.ruleBaseStatefulSession.startProcess(processName, vars);
    }

    @Override
    public boolean isDisableFactHandlerListener() {
        return this.ruleBaseStatefulSession.isDisableFactHandlerListener();
    }

    @Override
    public void setDisableFactHandlerListener(boolean disableFactHandlerListener) {
        this.ruleBaseStatefulSession.setDisableFactHandlerListener(disableFactHandlerListener);
    }



}
