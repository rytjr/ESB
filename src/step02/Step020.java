package step02;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Properties;

public class Step020 {
    private Log log = LogFactory.getLog(Step020.class);
    private Properties metaDataProperties;
    private int i = 0;

    public void init(){}

    public void onSignal(String arg) {
        log.debug("[==== onSignal start ====]");
        log.debug(arg + " :" + i++);

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[==== onSignal end ====]");
    }

    public Properties getMetaDataProperties() {
        return metaDataProperties;
    }

    public void setMetaDataProperties(Properties metaDataProperties) {this.metaDataProperties = metaDataProperties;}
}
