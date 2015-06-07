package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.entity.history.HistoryContainer;
import org.chtijbug.drools.platform.entity.event.PlatformKnowledgeBaseDisposeSessionEvent;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nheron on 07/06/15.
 */
public class SendToJMSThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(SendToJMSThread.class);

    private HistoryListener historyListener;

    private PlatformKnowledgeBaseDisposeSessionEvent platformKnowledgeBaseDisposeSessionEvent;

    public SendToJMSThread(HistoryListener historyListener, PlatformKnowledgeBaseDisposeSessionEvent platformKnowledgeBaseDisposeSessionEvent) {
        this.historyListener = historyListener;
        this.platformKnowledgeBaseDisposeSessionEvent = platformKnowledgeBaseDisposeSessionEvent;
    }

    @Override
    public void run() {
        if (historyListener!=null && platformKnowledgeBaseDisposeSessionEvent!=null){
            try {
                historyListener.fireEvent(platformKnowledgeBaseDisposeSessionEvent);
            } catch (DroolsChtijbugException e) {
                DroolsChtijbugException ee=new DroolsChtijbugException("run","SendToJMSThread",e);
               logger.error("SendToJMSThread.run",ee);
            }
        }

    }
}
