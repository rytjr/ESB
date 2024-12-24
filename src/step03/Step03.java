package step03;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public class Step03 {

    private final Log log = LogFactory.getLog(Step03.class);
    
    private String queueName;
    private JmsTemplate jmsTemplate;
    
    
    public void init(){
        String msg = "<root><header>header</header><body>body</body></root>";
        		
        log.debug("===== message send =====");
        sendMessage(msg); 
        log.debug("===== message end =====");
    }
    
    
    // string메세지를 JMS형식으로 변환하여  전송
    //구성된 JMS메시지에 txid라는 프로퍼티값을 1234로 세팅하여 전송 
    private void sendMessage(String msg){
        jmsTemplate.convertAndSend(getQueueName(), msg, new MessagePostProcessor(){
            public Message postProcessMessage(Message arg0) throws JMSException {
                arg0.setStringProperty("txid", "1234");
                return arg0;
            }
            
        });
    }
        
    //////////////////////////////
    // bean properties...
    //////////////////////////////
    public String getQueueName() {
        return queueName;
    }
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
