package org.chtijbug.drools.platform.core.websocket;

import org.chtijbug.drools.platform.entity.PlatformManagementKnowledgeBean;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/06/14
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public interface WebSocketServerInstance {
    public void end();

    public void run();

    public void sendHeartBeat();

    public void sendMessage(PlatformManagementKnowledgeBean platformManagementKnowledgeBean) throws DroolsChtijbugException;

    public abstract String getHostName();

    public abstract int getPort();
}
