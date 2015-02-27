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
package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.knowledgeBase;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseShutdownEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
public class PlatformKnowledgeBaseShutdownEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(PlatformKnowledgeBaseShutdownEventStrategy.class);


    @Autowired
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        PlatformKnowledgeBaseShutdownEvent platformKnowledgeBaseShutdownEvent = (PlatformKnowledgeBaseShutdownEvent) historyEvent;
        List<PlatformRuntimeInstance> existingPlatformRuntimeInstances = null;
        try {
            existingPlatformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndShutdowDateNull(platformKnowledgeBaseShutdownEvent.getRuleBaseID());
            if (existingPlatformRuntimeInstances.size() == 1) {
                existingPlatformRuntimeInstances.get(0).setShutdowDate(platformKnowledgeBaseShutdownEvent.getDateEvent());
                platformRuntimeInstanceRepository.save(existingPlatformRuntimeInstances.get(0));
            }
        } catch (Exception e) {
            LOG.error(platformKnowledgeBaseShutdownEvent, e);
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof PlatformKnowledgeBaseShutdownEvent;
    }

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        return true;
    }
}
