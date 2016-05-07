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

import org.chtijbug.drools.entity.history.KnowledgeResource;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.drools.runtime.listener.HistoryListener;


public interface DroolsPlatformKnowledgeBaseRuntime extends RuleBasePackage {
    java.util.List<KnowledgeResource> getDroolsResources();

    void setRuleBaseStatus(boolean b);

    String getGuvnorUsername();


    String getGuvnorPassword();


    boolean isReady();

    void disposePlatformRuleBaseSession(RuleBaseSession session) throws DroolsChtijbugException;

    void sendPlatformKnowledgeBaseInitialConnectionEventToServer() throws DroolsChtijbugException;


}
