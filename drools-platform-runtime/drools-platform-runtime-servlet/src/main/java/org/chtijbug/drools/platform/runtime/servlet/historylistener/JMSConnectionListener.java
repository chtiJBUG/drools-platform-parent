package org.chtijbug.drools.platform.runtime.servlet.historylistener;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/09/14
 * Time: 14:46
 * To change this template use File | Settings | File Templates.
 */

import javax.jms.Connection;
import javax.jms.JMSException;

public interface JMSConnectionListener {

    public void connected(Connection connection) throws JMSException, InterruptedException;
}
