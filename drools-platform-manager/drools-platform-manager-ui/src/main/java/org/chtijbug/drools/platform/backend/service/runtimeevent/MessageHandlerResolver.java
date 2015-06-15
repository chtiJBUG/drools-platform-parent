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
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


@Component
public class MessageHandlerResolver {
    @Resource
    private List<AbstractEventHandlerStrategy> allStrategies;

    @Resource
    private List<AbstractMemoryEventHandlerStrategy> allMemoryStrategies;

    public AbstractEventHandlerStrategy resolveMessageHandler(HistoryEvent historyEvent) {
        for (AbstractEventHandlerStrategy strategy : allStrategies) {
            if (strategy.isEventSupported(historyEvent))
                return strategy;
        }
        return null;
    }

    public AbstractMemoryEventHandlerStrategy resolveMessageHandlerMemory(HistoryEvent historyEvent) {
        for (AbstractMemoryEventHandlerStrategy strategy : allMemoryStrategies) {
            if (strategy.isEventSupported(historyEvent))
                return strategy;
        }
        return null;
    }
}
