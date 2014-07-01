package org.chtijbug.drools.platform.backend.service.runtimeevent;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractEventHandlerStrategy {


    @Transactional
    public void handleMessage(HistoryEvent historyEvent) {
        if (isEventSupported(historyEvent))
            handleMessageInternally(historyEvent);
    }

    protected abstract void handleMessageInternally(HistoryEvent historyEvent);

    public abstract boolean isEventSupported(HistoryEvent historyEvent);


}
