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
import org.chtijbug.drools.platform.core.websocket.WebSocketServerInstance;
import org.chtijbug.drools.platform.entity.JMXInfo;
import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.platform.entity.RequestRuntimePlarform;
import org.chtijbug.drools.platform.entity.RequestStatus;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.impl.RuleBaseStatefulSession;
import org.kie.api.runtime.ObjectFilter;

import java.util.Collection;

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



    @Override
    public void fireAllRules() throws DroolsChtijbugException {
        this.ruleBaseStatefulSession.fireAllRules();
    }

    @Override
    public Object fireAllRulesAndStartProcess(Object inputObject, String processName) throws DroolsChtijbugException {
        Object returnObject = this.ruleBaseStatefulSession.fireAllRulesAndStartProcess(inputObject, processName);
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
    public Long getSessionId() {
        return this.ruleBaseStatefulSession.getSessionId();
    }

    @Override
    public Long getRuleBaseID() {
        return this.ruleBaseStatefulSession.getRuleBaseID();
    }

    @Override
    public Collection<? extends java.lang.Object> getObjects(ObjectFilter objectFilter) {
        return this.ruleBaseStatefulSession.getObjects(objectFilter);
    }
}
