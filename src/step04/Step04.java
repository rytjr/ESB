package step04;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//MessageListener는 메시지 수신 관련 인터페이스 
//메시지 수신 클래스 Step04는 MessageListener를 상속받아 onMessage를 오버라이딩 하여 구현한다
public class Step04 implements MessageListener {

	private final Log log = LogFactory.getLog(Step04.class);

//설정된 큐에  메시지가 들어오면 처리한다.
	public void onMessage(Message message) {
		log.debug("==== onMessage Start ====");
		String textMsg = "";
		if (message instanceof TextMessage) {
			try {
				textMsg = ((TextMessage) message).getText();
				log.debug("Message Display : " + textMsg);
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Message must be of type TestMessage");
		}
		log.debug("==== onMessage End ====");

	}

}
