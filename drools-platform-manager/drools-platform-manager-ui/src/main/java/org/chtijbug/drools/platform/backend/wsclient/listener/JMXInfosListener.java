package org.chtijbug.drools.platform.backend.wsclient.listener;

import org.chtijbug.drools.platform.persistence.pojo.RealTimeParameters;

/**
 * Created by IntelliJ IDEA.
 * Date: 11/05/14
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public interface JMXInfosListener {
    public void messageReceived(Integer ruleBaseID,RealTimeParameters realTimeParameters);
}
