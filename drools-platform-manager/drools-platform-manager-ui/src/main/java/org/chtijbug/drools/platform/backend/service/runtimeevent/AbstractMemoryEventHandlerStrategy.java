package org.chtijbug.drools.platform.backend.service.runtimeevent;

import org.chtijbug.drools.entity.history.HistoryEvent;

/**
 * Created by nheron on 11/06/15.
 */
public abstract class AbstractMemoryEventHandlerStrategy {

    public abstract void handleMessageInternally(HistoryEvent historyEvent, SessionContext sessionContext);

    public abstract boolean isEventSupported(HistoryEvent historyEvent);


}
