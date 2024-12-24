package step01;

import java.util.Map;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Step01 {
    private final Log log = LogFactory.getLog(Step01.class);
    
    private String if_id;
    
    //bean.xml로 부터 주입되는 프로퍼티 인스턴스
    private Properties metaDataProperties;
    
    private Map<String, TestBean> beanMap ;
    
    public void init(){
        System.out.println("=== init start ====");
        log.debug(metaDataProperties.getProperty("queueName"));
        log.debug("if_id : " + if_id);
        
        System.out.println("=== bean print ====");
        log.debug(beanMap.get("b1").getName());
        log.debug(beanMap.get("b2").getName());

        log.debug(beanMap.get(beanMap.keySet().iterator().next()));
        Iterator it = beanMap.keySet().iterator();
        while(it.hasNext()){
        	log.debug(it.next());
        }
        System.out.println("=== init end ====");
    }
    
    ////////////////////////////////////
    //setter 방식으로 bean을 주입받기 위한 부분
    ///////////////////////////////////
    public Properties getMetaDataProperties() {
        return metaDataProperties;
    }

    public void setMetaDataProperties(Properties metaDataProperties) {
        this.metaDataProperties = metaDataProperties;
    }

    public String getIf_id() {
        return if_id;
    }

    public void setIf_id(String if_id) {
        this.if_id = if_id;
    }

    public Map<String,TestBean> getBeanMap() {
        return beanMap;
    }

    public void setBeanMap(Map<String,TestBean> beanMap) {
        this.beanMap = beanMap;
    }
}
