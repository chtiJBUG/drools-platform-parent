package org.chtijbug.drools.platform.runtime.servlet.historylistener;

import org.apache.log4j.Logger;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Date: 10/09/14
 * Time: 14:31
 * To change this template use File | Settings | File Templates.
 */
public class CreateJMSConnectionThread extends Thread {


    private int numberRetries;

    private ConnectionFactory factory;
    private long timeToWaitBetweenTwoRetries;
    JMSConnectionListener jmsConnectionListener;
    private static final Logger LOG = Logger.getLogger(ServletJmsStorageHistoryListener.class);

    public CreateJMSConnectionThread(JMSConnectionListener jmsConnectionListener, int numberRetries, ConnectionFactory factory, long timeToWaitBetweenTwoRetries) {
        this.numberRetries = numberRetries;
        this.jmsConnectionListener = jmsConnectionListener;
        this.factory = factory;
        this.timeToWaitBetweenTwoRetries = timeToWaitBetweenTwoRetries;
    }

    @Override
    public void run() {
        super.run();
        Connection connection;

        boolean connected = false;
        int retryNumber = 0;
        Exception lastException = null;
        while (retryNumber < numberRetries && connected == false) {
            try {
                connection = factory.createConnection();
                connected = true;
                this.jmsConnectionListener.connected(connection);
            } catch (Exception e) {
                lastException = e;
                try {

                    Thread.sleep(timeToWaitBetweenTwoRetries);
                } catch (InterruptedException e1) {
                    LOG.error("Could not  wait  Try number=" + retryNumber, e1);
                }
            } finally {
                retryNumber++;
            }
        }
        if (connected == false && retryNumber >= numberRetries) {
            if (lastException != null) {
                LOG.error("Could not Connect activeMQ after =" + retryNumber, lastException);
                if (lastException instanceof IOException) {
                    DroolsChtijbugException droolsChtijbugException = new DroolsChtijbugException("DroolsPlatformKnowledgeBaseJavaEE.initJmsConnection", "Could Not connect", lastException);
                    LOG.error("CreateJMSConnectionThread.run", droolsChtijbugException);
                }
            }
        }


    }
}
