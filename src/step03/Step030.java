package step03;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;

public class Step030 {

    private final Log log = LogFactory.getLog(Step030.class);

    private String queueName;
    private JmsTemplate jmsTemplate;

    public void init(){
        String msg = "<root><header>header</header><body>body</body></root>";

        log.debug("==== message send ====");
        sendMessage(msg);
        log.debug("==== message end ====");
    }

    private void sendMessage(String msg) {
        jmsTemplate.convertAndSend(getQueueName(), msg, new MessagePostProcessor() {
            public Message postProcessMessage(Message arg0) throws JMSException {
                arg0.setStringProperty("txid","1234");
                return arg0;
            }
        });
    }

    public  String getQueueName() {return queueName;}
    public void setQueueName(String queueName) {this.queueName = queueName;}
    public JmsTemplate getJmsTemplate() {return jmsTemplate;}
    public void setJmsTemplate(JmsTemplate jmsTemplate) {this.jmsTemplate = jmsTemplate;}
}
