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
package com.pymmasoftware.demo.loyalty.client;

import com.google.common.base.Throwables;
import loyalty.domains.*;
import org.chtijbug.drools.platform.runtime.javase.DroolsPlatformKnowledgeBaseJavaSE;
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
    private DroolsPlatformKnowledgeBaseJavaSE ruleBasePackage = null;


    public Ticket fireAllRules(Ticket ticket) throws InterruptedException {
        try {
            while (!ruleBasePackage.isReady()) {
                logger.info("Rule base package is not ready yet. Sleeping in the meantime.");
                Thread.sleep(2000);
            }
            RuleBaseSession sessionStatefull = ruleBasePackage.createRuleBaseSession();
            sessionStatefull.fireAllRulesAndStartProcess(ticket, "P1");
            ruleBasePackage.disposePlatformRuleBaseSession(sessionStatefull);
        } catch (DroolsChtijbugException e) {
            logger.error("Error in fireallrules", e);
            throw Throwables.propagate(e);
        }
        return ticket;
    }

    public void setRuleBasePackage(DroolsPlatformKnowledgeBaseJavaSE ruleBasePackage) {
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
        Provider provider = new Provider();
        provider.setName("Pymma Software");
        provider.setCountry("fr");
        Price price = new Price();
        price.setCurrency(Currency.Euro);
        price.setPrice(new Float("100.0"));
        Product product = new Product();
        product.setPrice(price);
        product.setProvider(provider);
        product.setId("100-100");
        product.setName("Pampers");
        ticket.AddLine(product, new Float("100.0"), 10);


        while (true) {
            ticket = loyaltyClient.fireAllRules(ticket);
            logger.info("Ticket processed : {}", ticket);
            sleep(30000);
        }

    }

}
