package org.chtijbug.drools.platform.backend.service.runtimeevent.impl.fact;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.fact.InsertedByReflectionFactStartHistoryEvent;
import org.chtijbug.drools.platform.backend.service.runtimeevent.AbstractEventHandlerStrategy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class InsertedByRelectionFactStartEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(InsertedByRelectionFactStartEventStrategy.class);


    @Override
    @Transactional
    protected void handleMessageInternally(HistoryEvent historyEvent) {

        LOG.debug("InsertedByReflectionFactStartHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof InsertedByReflectionFactStartHistoryEvent;
    }
}
