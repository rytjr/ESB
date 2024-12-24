package model;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

public class Row implements Serializable {
    private static final long serialVersionUID = 1L;

    private String NAME;
    private String EMPNO;
    private String TEL;
    private String LINK_STATE_CD;

    @XmlElement(name = "NAME")
    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @XmlElement(name = "EMPNO")
    public String getEMPNO() {
        return EMPNO;
    }

    public void setEMPNO(String EMPNO) {
        this.EMPNO = EMPNO;
    }

    @XmlElement(name = "TEL")
    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    @XmlElement(name = "LINK_STATE_CD")
    public String getLINK_STATE_CD() {
        return LINK_STATE_CD;
    }

    public void setLINK_STATE_CD(String LINK_STATE_CD) {
        this.LINK_STATE_CD = LINK_STATE_CD;
    }

    @Override
    public String toString() {
        return "Row { " +
                "NAME='" + NAME + "', " +
                "EMPNO='" + EMPNO + "', " +
                "TEL='" + TEL + "', " +
                "LINK_STATE_CD='" + LINK_STATE_CD + "' " +
                "}";
    }
}




