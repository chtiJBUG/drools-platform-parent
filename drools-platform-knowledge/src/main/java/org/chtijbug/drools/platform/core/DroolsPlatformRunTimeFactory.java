package org.chtijbug.drools.platform.core;

import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * Date: 19/04/14
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
public class DroolsPlatformRunTimeFactory {
    private static Logger logger = LoggerFactory.getLogger(DroolsPlatformRunTimeFactory.class);

    public static RuleBasePackage createGuvnorRuleBasePackageWithListener(String guvnor_url, String guvnor_appName, String guvnor_packageName, String guvnor_packageVersion,
                                                                          String guvnor_username, String guvnor_password) throws DroolsChtijbugException {
        logger.debug(">>createGuvnorRuleBasePackage");

        RuleBasePackage ruleBasePackage = new DroolsPlatformKnowledgeBase(guvnor_url, guvnor_appName, guvnor_packageName, guvnor_packageVersion, guvnor_username, guvnor_password);
        logger.debug("<<createGuvnorRuleBasePackage", ruleBasePackage);

        return ruleBasePackage;
    }


    public static RuleBasePackage createPackageBasePackageWithListener(String... filenames) throws DroolsChtijbugException {
        logger.debug(">>createPackageBasePackage");
        RuleBasePackage ruleBasePackage = new DroolsPlatformKnowledgeBase(filenames);
         //_____ Returning the result
        logger.debug("<<createPackageBasePackage", ruleBasePackage);
        return ruleBasePackage;
    }


}
