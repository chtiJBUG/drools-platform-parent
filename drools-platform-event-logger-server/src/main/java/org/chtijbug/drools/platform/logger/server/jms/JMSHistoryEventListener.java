package org.chtijbug.drools.platform.logger.server.jms;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;


/**
 * Created by IntelliJ IDEA.
 * Date: 20/12/13
 * Time: 15:20
 * To change this template use File | Settings | File Templates.
 */
public class JMSHistoryEventListener implements MessageListener {

  private static final Logger LOG = Logger.getLogger(JMSHistoryEventListener.class);

  public void onMessage(Message message) {
      try {
       TextMessage msg = (TextMessage) message;
       LOG.info("Consumed message: " + msg.getText());
      } catch (JMSException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
  }

}