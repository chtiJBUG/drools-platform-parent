package org.chtijbug.drools.platform.core.droolslistener;

import org.chtijbug.drools.runtime.listener.HistoryListener;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/06/14
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public interface PlatformHistoryListener extends HistoryListener {
    void shutdown();
}
