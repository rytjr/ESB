package step05;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public class Step05 {
	
	    private final Log log = LogFactory.getLog(Step05.class);
	    
	    private String queueName;
	    private JmsTemplate jmsTemplate;
	    
	    public void onSignal(){
	        String msg = getMsg(); 
	        log.debug("===== message send =====");
	        sendMessage(msg);
	        log.debug("===== message end ====="); 
	    }
	    
	    private void sendMessage(String msg){
	       
	        jmsTemplate.convertAndSend(getQueueName(), msg, new MessagePostProcessor(){
	            public Message postProcessMessage(Message arg0) throws JMSException {
	                arg0.setStringProperty("txid", "1234");
	                return arg0;
	            }
	        });
	    }
	//스케줄러가 설정된 시간간격으로 onSignal() 을 호출할 때마다 증가된 i값을 이용하여 송신메시지를 만들어낸다.    
	    int i=0;
	    public String getMsg(){
	    	return "<root><header></header><body>"+(i++)+"</body></root>";
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
