package swimmingpool.service;

import org.chtijbug.drools.platform.runtime.servlet.DroolsPlatformKnowledgeBaseJavaEE;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.chtijbug.example.swimmingpool.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "swimmingpool.service.IServiceCalculate")
public class ServiceCalculate implements IServiceCalculate {
    private static Logger logger = LoggerFactory.getLogger(ServiceCalculate.class);

    private DroolsPlatformKnowledgeBaseJavaEE platformKnowledgeBaseJavaEE;

    public void setRuleBasePackage(DroolsPlatformKnowledgeBaseJavaEE ruleBasePackage) {
        this.platformKnowledgeBaseJavaEE = ruleBasePackage;
    }

    @Override
    public Quote calculate(@WebParam(name = "abonnement") Quote quote) {
        RuleBaseSession sessionStatefull = null;
        try {
            sessionStatefull = platformKnowledgeBaseJavaEE.createRuleBaseSession();
            sessionStatefull.fireAllRulesAndStartProcess(quote, "P001");
            sessionStatefull.dispose();
        } catch (DroolsChtijbugException e) {
            logger.error("Error in fireallrules", e);
        }
        return quote;
    }
}
