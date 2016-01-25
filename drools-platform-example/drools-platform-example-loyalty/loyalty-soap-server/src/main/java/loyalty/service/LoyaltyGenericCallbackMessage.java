package loyalty.service;

import org.chtijbug.drools.platform.core.callback.SpecificMessageCallback;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by nheron on 20/01/2016.
 */
public class LoyaltyGenericCallbackMessage implements SpecificMessageCallback {
    @Override
    public void handleMessage(String messageId, String messageContent) {
        System.out.println("MessageID= "+messageId+" MessagContent= "+messageContent);
    }
}
