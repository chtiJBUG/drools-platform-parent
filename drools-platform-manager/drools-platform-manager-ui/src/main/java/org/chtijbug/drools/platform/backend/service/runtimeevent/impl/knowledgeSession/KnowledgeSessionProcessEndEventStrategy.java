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
import org.chtijbug.drools.entity.history.session.SessionStartProcessEndEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractMemoryEventHandlerStrategy;
import org.chtijbug.drools.platform.backend.service.runtimeevent.SessionContext;
import org.springframework.stereotype.Component;


@Component
public class KnowledgeSessionProcessEndEventStrategy extends AbstractMemoryEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(KnowledgeSessionProcessEndEventStrategy.class);


    @Override
    public void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext) {


        LOG.debug("SessionStartProcessEndEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof SessionStartProcessEndEvent;
    }
}
