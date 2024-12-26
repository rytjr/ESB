package step05;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.Message;
import javax.jms.JMSException;

public class Step050 {

    private final Log log = LogFactory.getLog(Step050.class);

    private String queueName;
    private JmsTemplate jmsTemplate;

    public void onSignal() {
        String msg = getMsg();
        log.debug("==== message send ====");
        sendMessage(msg);
        log.debug("==== message end ====");
    }

    private void sendMessage(String msg) {
        jmsTemplate.convertAndSend(getQueueName(), msg, new MessagePostProcessor() {
            public Message postProcessMessage(Message arg0) throws JMSException {
                arg0.setStringProperty("txid", "1234");
                return arg0;
            }
        });
    }

    int i = 0;
    public String getMsg() {return "<root><header></header><body>" + (i++) + "</body></root>";}

    public String getQueueName() {return queueName;}
    public void setQueueName(String queueName) {this.queueName = queueName;}
    public JmsTemplate getJmsTemplate() {return jmsTemplate;}
    public void setJmsTemplate(JmsTemplate jmsTemplate) {this.jmsTemplate = jmsTemplate;}
}
