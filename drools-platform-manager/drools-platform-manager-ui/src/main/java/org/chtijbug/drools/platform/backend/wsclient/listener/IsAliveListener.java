package org.chtijbug.drools.platform.backend.wsclient.listener;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 09:27
 * To change this template use File | Settings | File Templates.
 */
public interface IsAliveListener {
    public void messageReceived(Integer ruleBaseID);
}
