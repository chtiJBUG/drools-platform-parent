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
package loyalty.service;

import loyalty.domains.Ticket;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "loyalty.service.IServiceCalculate")
public class ServiceCalculate implements IServiceCalculate {
    private static Logger logger = LoggerFactory.getLogger(ServiceCalculate.class);

    private DroolsPlatformKnowledgeBase platformKnowledgeBaseJavaEE;

    public void setRuleBasePackage(DroolsPlatformKnowledgeBase ruleBasePackage) {
        this.platformKnowledgeBaseJavaEE = ruleBasePackage;
    }

    @Override
    public Ticket calculate(@WebParam(name = "ticket") Ticket ticket) {
        RuleBaseSession sessionStatefull = null;
        try {
            sessionStatefull = platformKnowledgeBaseJavaEE.createRuleBaseSession();
            sessionStatefull.fireAllRulesAndStartProcess(ticket, "P1.P1");
            platformKnowledgeBaseJavaEE.disposePlatformRuleBaseSession(sessionStatefull);

        } catch (DroolsChtijbugException e) {
            logger.error("Error in fireallrules", e);
        }
        return ticket;
    }
}
