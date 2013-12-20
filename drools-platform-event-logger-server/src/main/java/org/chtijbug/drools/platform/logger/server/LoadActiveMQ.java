package org.chtijbug.drools.platform.logger.server;

import org.apache.activemq.broker.BrokerService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoadActiveMQ extends HttpServlet {

    /**
     * serialVersionUID for serialization
     */
    private static final long serialVersionUID = -8289081958495740549L;

    private BrokerService broker;

    private static final Logger logger = Logger.getLogger(LoadActiveMQ.class
            .getName());

    @Override
    public void init() throws ServletException {
// / Automatically java script can run here
        logger.info("Load activeMQ");
// configure the broker
        try {
            broker = new BrokerService();
            broker.addConnector("tcp://localhost:61616");
            broker.start();
            logger.info("ActiveMQ loaded succesfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to load ActiveMQ!");
        }
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public void destroy() {
        try {
            logger.info("ActiveMQ exiting");
            broker.stop();
            logger.info("ActiveMQ exit succesfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to exit ActiveMQ!");
        }
    }
}
