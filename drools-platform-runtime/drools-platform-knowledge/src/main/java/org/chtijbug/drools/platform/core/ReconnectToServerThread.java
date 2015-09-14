package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.platform.core.wssocket.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by nheron on 07/06/15.
 */
public class ReconnectToServerThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ReconnectToServerThread.class);

    private WebSocketClient webSocketClient;

    private boolean isRunning = false;

    public ReconnectToServerThread(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    public void run() {
        if (isRunning == false) {
            isRunning = true;
            try {
                this.webSocketClient.connectToServer();
            } catch (IOException e) {
                logger.error("ReconnectToServerThread", e);
            }
            isRunning = false;
        }
    }
}
