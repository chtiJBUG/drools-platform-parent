/*
 * Copyright 2014 Pymma Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chtijbug.drools.platform.runtime.servlet.historylistener;

import org.apache.log4j.Logger;
import org.chtijbug.drools.runtime.DroolsChtijbugException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import java.io.IOException;


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
