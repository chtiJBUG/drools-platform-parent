package com.pymmasoftware.demo.loyalty.client;

import com.google.common.base.Throwables;
import loyalty.domains.*;
import org.chtijbug.drools.platform.core.DroolsPlatformKnowledgeBase;
import org.chtijbug.drools.runtime.DroolsChtijbugException;
import org.chtijbug.drools.runtime.RuleBaseSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.lang.Thread.sleep;

public class StandaloneLoyaltyClient {
    private static Logger logger = LoggerFactory.getLogger(StandaloneLoyaltyClient.class);
    private DroolsPlatformKnowledgeBase ruleBasePackage = null;


    public Ticket fireAllRules(Ticket ticket) throws InterruptedException {
        try {
            while (!ruleBasePackage.isReady()){
                logger.info("Rule base package is not ready yet. Sleeping in the meantime.");
                Thread.sleep(2000);
            }
            RuleBaseSession sessionStatefull = ruleBasePackage.createRuleBaseSession();
            sessionStatefull.insertByReflection(ticket);
            sessionStatefull.startProcess("P1");
            sessionStatefull.fireAllRules();
            sessionStatefull.dispose();
        } catch (DroolsChtijbugException e) {
            logger.error("Error in fireallrules", e);
            throw Throwables.propagate(e);
        }
        return ticket;
    }

    public void setRuleBasePackage(DroolsPlatformKnowledgeBase ruleBasePackage) {
        this.ruleBasePackage = ruleBasePackage;
    }


    public static void main(String[] args) throws InterruptedException, ParseException {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/application-context.xml");

        StandaloneLoyaltyClient loyaltyClient = context.getBean(StandaloneLoyaltyClient.class);
        Ticket ticket = new Ticket();
        ticket.setAmount(1000.0f);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ticket.setDateTicket(sdf.parse("01/01/2012"));
        ticket.setId("1");

        Card loyaltyCard = new Card();
        loyaltyCard.setCartType("Gold");
        loyaltyCard.setName("VISA");
        loyaltyCard.setName("4859569558");
        Customer customer = new Customer();
        customer.setCustomerID("12");
        customer.setBirthDate(sdf.parse("23/08/1968"));
        customer.setName("Heron");
        customer.setSurName("Nicolas");
        customer.setGender(Gender.Mr);
        loyaltyCard.setCustomer(customer);
        ticket.setLoyaltyCard(loyaltyCard);
        TicketLine line1 = new TicketLine();
        line1.setLineNumber(1);
        line1.setLineTotal(100f);
        line1.setPrice(120f);
        line1.setProductID("12");
        line1.setQuantity(5);
        line1.setTicket(ticket);
        line1.setTicketID(ticket.getId());
        line1.setValid(true);
        Product product = new Product();
        product.setId("12");
        product.setName("pampers");
        Price price = new Price();
        price.setCurrency(Currency.Euro);
        price.setPrice(12.0f);
        product.setPrice(price);
        Provider provider = new Provider();
        provider.setName("Nobleprog");
        provider.setCountry("GB");
        product.setProvider(provider);

        line1.setProduct(product);


        while (true) {
            ticket = loyaltyClient.fireAllRules(ticket);
            logger.info("Ticket processed : {}", ticket);
            sleep(3000);
        }

    }

}
