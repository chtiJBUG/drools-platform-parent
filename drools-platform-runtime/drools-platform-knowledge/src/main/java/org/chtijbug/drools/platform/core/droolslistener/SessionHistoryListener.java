package org.chtijbug.drools.platform.core.droolslistener;

import org.chtijbug.drools.entity.history.HistoryEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nheron on 07/06/15.
 */
public class SessionHistoryListener implements PlatformHistoryListener{

    private List<HistoryEvent> historyEvents= new ArrayList<>();

    public SessionHistoryListener() {
    }

    public List<HistoryEvent> getHistoryEvents() {
        return historyEvents;
    }

    @Override
    public void shutdown() {
        this.historyEvents.clear();

    }

    @Override
    public void fireEvent(HistoryEvent newHistoryEvent) throws DroolsChtijbugException {
        if (this.historyEvents!=null){
            this.historyEvents.add(newHistoryEvent);
        }
    }
}
