package org.chtijbug.drools.platform.backend.wsclient.listener;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Date: 12/05/14
 * Time: 09:23
 * To change this template use File | Settings | File Templates.
 */
public interface HeartBeatListner {
    public void messageReceived(Integer ruleBaseID,Date date);
}
