package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.resource.DroolsResource;

/**
 * Created by IntelliJ IDEA.
 * Date: 13/06/14
 * Time: 13:05
 * To change this template use File | Settings | File Templates.
 */
public interface DroolsPlatformKnowledgeBaseRuntime extends RuleBasePackage {
    java.util.List<DroolsResource> getDroolsResources();

    void setRuleBaseStatus(boolean b);

    String getGuvnorUsername();


    String getGuvnorPassword();


    boolean isReady();
}
