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
package org.chtijbug.drools.platform.backend.service.runtimeevent;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeDefinitionRepository;
import org.chtijbug.drools.platform.persistence.PlatformRuntimeDefinitionRepositoryCacheService;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeDefinition;
import org.chtijbug.drools.platform.persistence.pojo.PlatformRuntimeMode;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractEventHandlerStrategy {

    @Autowired
    PlatformRuntimeDefinitionRepositoryCacheService platformRuntimeDefinitionRepositoryCacheService;

    public void handleMessage(HistoryEvent historyEvent) {
        PlatformRuntimeDefinition platformRuntimeDefinition = platformRuntimeDefinitionRepositoryCacheService.findByRuleBaseID(historyEvent.getRuleBaseID());
        if (isEventSupported(historyEvent)) {
            if (platformRuntimeDefinition!=null){
                PlatformRuntimeMode platformRuntimeMode = platformRuntimeDefinition.getPlatformRuntimeMode();
                if (isLevelCompatible(platformRuntimeMode)) {
                    handleMessageInternally(historyEvent);
                }
            }else{
                handleMessageInternally(historyEvent);
            }

        }
    }

    protected abstract void handleMessageInternally(HistoryEvent historyEvent);

    public abstract boolean isEventSupported(HistoryEvent historyEvent);

    public abstract boolean isLevelCompatible(PlatformRuntimeMode platformRuntimeMode);
}
