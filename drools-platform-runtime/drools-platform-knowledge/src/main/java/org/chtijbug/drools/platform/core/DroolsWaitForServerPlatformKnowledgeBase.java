package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.impl.JavaDialect;
import org.chtijbug.drools.runtime.resource.DroolsResource;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by nheron on 14/09/15.
 */
public class DroolsWaitForServerPlatformKnowledgeBase extends DroolsPlatformKnowledgeBase {
    public DroolsWaitForServerPlatformKnowledgeBase(Integer ruleBaseID, List<DroolsResource> droolsResources, JavaDialect javaDialect) throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        super(ruleBaseID, droolsResources, javaDialect);
    }

    public DroolsWaitForServerPlatformKnowledgeBase() {
    }

    @Override
    public void initPlatformRuntime() throws InterruptedException, DroolsChtijbugException, UnknownHostException {

        WaitForServerThread waitForServerThread = new WaitForServerThread(this);
        waitForServerThread.run();


    }

    public void initFatherPlatformRuntime() throws InterruptedException, DroolsChtijbugException, UnknownHostException {
        super.initPlatformRuntime();
    }
}
