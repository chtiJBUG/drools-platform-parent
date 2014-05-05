package org.chtijbug.drools.platform.backend.service.impl.process;

import org.apache.log4j.Logger;
import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.entity.history.process.BeforeVariableChangeChangedHistoryEvent;
import org.chtijbug.drools.platform.backend.service.AbstractEventHandlerStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Date: 25/03/14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */

@Component
public class BeforeVariableChangeEventStrategy extends AbstractEventHandlerStrategy {
    private static final Logger LOG = Logger.getLogger(BeforeVariableChangeEventStrategy.class);


    @Override
    protected void handleMessageInternally(HistoryEvent historyEvent) {

        LOG.debug("BeforeVariableChangeChangedHistoryEvent " + historyEvent.toString());
    }

    @Override
    public boolean isEventSupported(HistoryEvent historyEvent) {

        return historyEvent instanceof BeforeVariableChangeChangedHistoryEvent;
    }
}
