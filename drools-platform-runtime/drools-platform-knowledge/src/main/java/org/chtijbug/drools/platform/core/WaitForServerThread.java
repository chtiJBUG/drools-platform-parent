package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.listener.HistoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * Created by nheron on 07/06/15.
 */
public class WaitForServerThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(WaitForServerThread.class);

    private HistoryListener historyListener;

    private DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase;

    public WaitForServerThread(DroolsPlatformKnowledgeBase droolsPlatformKnowledgeBase) {

        this.droolsPlatformKnowledgeBase = droolsPlatformKnowledgeBase;
    }

    @Override
    public void run() {
        if (droolsPlatformKnowledgeBase != null) {
            try {
                droolsPlatformKnowledgeBase.initPlatformRuntime();
            } catch (InterruptedException e) {
                logger.error("WaitForServerThread", e);

            } catch (DroolsChtijbugException e) {
                logger.error("WaitForServerThread", e);
            } catch (UnknownHostException e) {
                logger.error("WaitForServerThread", e);
            }
        }

    }
}
