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
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeSession;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.DroolsFactObject;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesAndStartProcess;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.Fact;
import org.chtijbug.drools.platform.persistence.pojo.FactType;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.chtijbug.drools.platform.persistence.pojo.SessionExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class KnowledgeSessionFireAllRulesAndStartProcessEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesAndStartProcessEventStrategy.class);

    @Autowired
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesAndStartProcess sessionFireAllRulesAndStartProcess = (SessionFireAllRulesAndStartProcess) historyEvent;
        SessionExecution existingSessionRutime = sessionExecutionRepository.findByRuleBaseIDAndSessionIdAndEndDateIsNull(historyEvent.getRuleBaseID(), historyEvent.getSessionId());
        if (existingSessionRutime != null) {
            if (sessionFireAllRulesAndStartProcess.getInputObject() != null) {
                DroolsFactObject inputObject = sessionFireAllRulesAndStartProcess.getInputObject();
                Fact inputFact = new Fact();
                inputFact.setEventid(sessionFireAllRulesAndStartProcess.getEventID());
                inputFact.setFactType(FactType.INPUTDATA);
                inputFact.setFullClassName(inputObject.getFullClassName());
                inputFact.setJsonFact(inputObject.getRealObject_JSON());
                inputFact.setModificationDate(sessionFireAllRulesAndStartProcess.getDateEvent());
                inputFact.setObjectVersion(inputObject.getObjectVersion());
                existingSessionRutime.getFacts().add(inputFact);
            }
            if (sessionFireAllRulesAndStartProcess.getOutputObject() != null) {
                DroolsFactObject outputObject = sessionFireAllRulesAndStartProcess.getOutputObject();
                Fact outputFact = new Fact();
                outputFact.setEventid(sessionFireAllRulesAndStartProcess.getEventID());
                outputFact.setFactType(FactType.OUTPUTDATA);
                outputFact.setFullClassName(outputObject.getFullClassName());
                outputFact.setJsonFact(outputObject.getRealObject_JSON());
                outputFact.setModificationDate(sessionFireAllRulesAndStartProcess.getDateEvent());
                outputFact.setObjectVersion(outputObject.getObjectVersion());
                existingSessionRutime.getFacts().add(outputFact);
            }

            sessionExecutionRepository.save(sessionFireAllRulesAndStartProcess.getRuleBaseID(), sessionFireAllRulesAndStartProcess.getSessionId(), existingSessionRutime);
        }

        LOG.debug("SessionFireAllRulesAndStartProcess " + sessionFireAllRulesAndStartProcess.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesAndStartProcess;
    }

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        if (platformRuntimeMode==PlatformRuntimeMode.Debug) {
            return true;
        }
        else{
            return false;
        }
    }
}
