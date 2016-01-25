package org.chtijbug.drools.platform.core.callback;

/**
 * Created by nheron on 20/01/2016.
 */
public interface SpecificMessageCallback {


    public void handleMessage(String messageId, String messageContent);
}
