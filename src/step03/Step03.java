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
    
    
    // string�޼����� JMS�������� ��ȯ�Ͽ�  ����
    //������ JMS�޽����� txid��� ������Ƽ���� 1234�� �����Ͽ� ���� 
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
