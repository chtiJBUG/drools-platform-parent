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
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddResourceEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepository;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.chtijbug.drools.runtime.resource.FileKnowledgeResource;
import org.chtijbug.drools.runtime.resource.WorkbenchKnowledgeResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class KnowledgeBaseAddRessourceEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseAddRessourceEventStrategy.class);


    @Autowired
    PlatformRuntimeInstanceRepository platformRuntimeInstanceRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseAddResourceEvent knowledgeBaseAddRessourceEvent = (KnowledgeBaseAddResourceEvent) historyEvent;
        List<PlatformRuntimeInstance> existingPlatformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseAddRessourceEvent.getRuleBaseID());
        if (existingPlatformRuntimeInstances.size() == 1) {
            PlatformRuntimeInstance existingPlatformRuntimeInstance = existingPlatformRuntimeInstances.get(0);

            DroolsResource droolsResource;
            /**
             *  KnowledgeBaseAddRessourceEvent sends only one droolsressources
             */
            if (knowledgeBaseAddRessourceEvent.getKnowledgeResources().size() == 1 && knowledgeBaseAddRessourceEvent.getKnowledgeResources().get(0) instanceof WorkbenchKnowledgeResource) {
                WorkbenchKnowledgeResource workbenchKnowledgeResource = (WorkbenchKnowledgeResource) knowledgeBaseAddRessourceEvent.getKnowledgeResources().get(0);
                droolsResource = new DroolsResource(workbenchKnowledgeResource.getGuvnor_url(), workbenchKnowledgeResource.getGroupId(), workbenchKnowledgeResource.getArtifactID(), workbenchKnowledgeResource.getVersion());
            } else {
                FileKnowledgeResource fileKnowledgeResource = (FileKnowledgeResource) knowledgeBaseAddRessourceEvent.getKnowledgeResources().get(0);
                droolsResource = new DroolsResource(fileKnowledgeResource.getPath(), fileKnowledgeResource.getContent());
            }
            droolsResource.setStartDate(knowledgeBaseAddRessourceEvent.getDateEvent());
            droolsResource.setStartEventID(knowledgeBaseAddRessourceEvent.getEventID());
            existingPlatformRuntimeInstance.getDroolsRessources().add(droolsResource);
            platformRuntimeInstanceRepository.save(existingPlatformRuntimeInstance);
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseAddResourceEvent;
    }

    @Override
    public boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode) {
        return true;
    }
}
