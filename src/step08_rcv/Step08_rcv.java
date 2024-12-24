package step08_rcv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

//MessageListener�� �޽��� ���� ���� �������̽� 
//�޽��� ���� Ŭ���� Step04�� MessageListener�� ��ӹ޾� onMessage�� �������̵� �Ͽ� �����Ѵ�
public class Step08_rcv implements MessageListener {

	private final Log log = LogFactory.getLog(Step08_rcv.class);

//������ ť��  �޽����� ������ ó���Ѵ�.
	public void onMessage(Message message) {
		log.debug("==== onMessage Start ====");
		String textMsg = "";
		if (message instanceof TextMessage) {
			try {
				textMsg = ((TextMessage) message).getText();
				log.debug("received Message : " + textMsg);
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Message must be of type TestMessage");
		}
		log.debug("==== onMessage End ====");

	}

}
