package loyalty.service;

import loyalty.domains.Ticket;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBasePackage;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "loyalty.service.IServiceCalculate")
public class ServiceCalculate implements IServiceCalculate {


    private static Logger logger = LoggerFactory.getLogger(ServiceCalculate.class);


    private RuleBasePackage ruleBasePackage = null;

    public void setRuleBasePackage(RuleBasePackage ruleBasePackage) {
        this.ruleBasePackage = ruleBasePackage;
    }


    @Override
    public Ticket calculate(@WebParam(name = "ticket") Ticket ticket) {


        RuleBaseSession sessionStatefull = null;
        try {
            sessionStatefull = ruleBasePackage.createRuleBaseSession();
            sessionStatefull.insertByReflection(ticket);
            sessionStatefull.startProcess("P1");
            sessionStatefull.fireAllRules();
            sessionStatefull.dispose();
        } catch (DroolsChtijbugException e) {
            logger.error("Error in fireallrules", e);
        }
        return ticket;
    }

    @Override
    public String reloadPackageVersion() throws DroolsChtijbugException {
        ruleBasePackage.ReloadWithSameRessources();
        return "New Rule Base Package version  loaded";
    }
}
