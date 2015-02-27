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
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.session.SessionFireAllRulesEndEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.FireAllRulesExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.SessionExecutionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecution;
import org.chtijbug.drools.platform.persistence.pojo.FireAllRulesExecutionStatus;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class KnowledgeSessionFireAllRulesEndEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionFireAllRulesEndEventStrategy.class);

    @Autowired
    FireAllRulesExecutionRepositoryCacheService fireAllRulesExecutionRepository;

    @Autowired
    SessionExecutionRepositoryCacheService sessionExecutionRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        SessionFireAllRulesEndEvent sessionFireAllRulesEndEvent = (SessionFireAllRulesEndEvent) historyEvent;
        FireAllRulesExecution fireAllRulesExecution = fireAllRulesExecutionRepository.findStartedFireAllRulesBySessionID(historyEvent.getSessionId());
        fireAllRulesExecution.setStopEventID(sessionFireAllRulesEndEvent.getEventID());
        fireAllRulesExecution.setEndDate(sessionFireAllRulesEndEvent.getDateEvent());
        fireAllRulesExecution.setFireAllRulesExecutionStatus(FireAllRulesExecutionStatus.STOPPED);
        fireAllRulesExecution.setNbreRulesFired(Long.valueOf(sessionFireAllRulesEndEvent.getNumberRulesExecuted()));
        fireAllRulesExecution.setExecutionTime(Long.valueOf(sessionFireAllRulesEndEvent.getExecutionTime()));
        fireAllRulesExecutionRepository.save(fireAllRulesExecution);
        LOG.debug("SessionFireAllRulesEndEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionFireAllRulesEndEvent;
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
