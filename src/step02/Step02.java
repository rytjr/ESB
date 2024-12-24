package step02;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

	public class Step02 {    
		private Log log = LogFactory.getLog(Step02.class);
	    private Properties metaDataProperties;
	    private int i=0;
	    
	    public void init(){}
	    
	    public void onSignal(String arg){
	        log.debug("[===== onSignal start ======]");
	        log.debug(arg+" :" + i++ );
//실제 업무에 15초가 소요됨을 가정하여 구현 
       try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	        System.out.println("[===== onSignal end   ======]");
	    }
	    ////////////////////////////////////
	    //  bean properties... 
	    ///////////////////////////////////
	    public Properties getMetaDataProperties() {
	        return metaDataProperties;
	    }

	    public void setMetaDataProperties(Properties metaDataProperties) {
	        this.metaDataProperties = metaDataProperties;
	    }
	    
	}
