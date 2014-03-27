package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.AfterNodeInstanceTriggeredHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class AfterNodeInstanceTriggeredEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(AfterNodeInstanceTriggeredEventStrategy.class);

    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {
        LOG.info("AfterNodeInstanceTriggeredHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof AfterNodeInstanceTriggeredHistoryEvent;
    }
}
