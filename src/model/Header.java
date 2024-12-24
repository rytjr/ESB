package model;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class Header implements Serializable {
    private static final long serialVersionUID = 1L;

    private String time;

    @XmlElement(name = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Header { time='" + time + "' }";
    }
}



