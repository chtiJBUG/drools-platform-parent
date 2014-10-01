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
import org.chtijbug.drools.entity.history.DrlResourceFile;
import org.chtijbug.drools.entity.history.GuvnorResourceFile;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.knowledge.KnowledgeBaseAddRessourceEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeInstanceRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.DroolsResource;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class KnowledgeBaseAddRessourceEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeBaseAddRessourceEventStrategy.class);


    @Autowired
    PlatformRuntimeInstanceRepositoryCacheService platformRuntimeInstanceRepository;

    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        KnowledgeBaseAddRessourceEvent knowledgeBaseAddRessourceEvent = (KnowledgeBaseAddRessourceEvent) historyEvent;
        List<PlatformRuntimeInstance> existingPlatformRuntimeInstances = platformRuntimeInstanceRepository.findByRuleBaseIDAndEndDateNull(knowledgeBaseAddRessourceEvent.getRuleBaseID());
        if (existingPlatformRuntimeInstances.size() == 1) {
            PlatformRuntimeInstance existingPlatformRuntimeInstance = existingPlatformRuntimeInstances.get(0);

            DroolsResource droolsResource;
            /**
             *  KnowledgeBaseAddRessourceEvent sends only one droolsressources
             */
            if (knowledgeBaseAddRessourceEvent.getResourceFiles().size() == 1 && knowledgeBaseAddRessourceEvent.getResourceFiles().get(0) instanceof GuvnorResourceFile) {
                GuvnorResourceFile guvnorResourceFile = (GuvnorResourceFile) knowledgeBaseAddRessourceEvent.getResourceFiles().get(0);
                droolsResource = new DroolsResource(guvnorResourceFile.getGuvnor_url(), guvnorResourceFile.getGuvnor_appName(), guvnorResourceFile.getGuvnor_packageName(), guvnorResourceFile.getGuvnor_packageVersion());
            } else {
                DrlResourceFile drlResourceFile = (DrlResourceFile) knowledgeBaseAddRessourceEvent.getResourceFiles().get(0);
                droolsResource = new DroolsResource(drlResourceFile.getFileName(), drlResourceFile.getContent());
            }
            droolsResource.setStartDate(knowledgeBaseAddRessourceEvent.getDateEvent());
            droolsResource.setStartEventID(knowledgeBaseAddRessourceEvent.getEventID());
            existingPlatformRuntimeInstance.getDroolsRessources().add(droolsResource);
            platformRuntimeInstanceRepository.save(existingPlatformRuntimeInstance);
        }
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {
        return historyEvent instanceof KnowledgeBaseAddRessourceEvent;
    }
}
