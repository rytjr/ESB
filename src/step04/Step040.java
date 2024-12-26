package step04;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Step040 implements MessageListener {

	private final Log log = LogFactory.getLog(Step040.class);

	public void onMessage(Message message) {
		log.debug("==== onMessage Start ====");
		String textMsg = "";
		if(message instanceof TextMessage) {
			try {
				textMsg = ((TextMessage) message).getText();
				log.debug("Message Display : " + textMsg);
			}catch (JMSException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Message must be of type TestMessage");
		}
		log.debug("==== onMessage End ====");
	}


}
