package step07;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

import java.sql.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.sql.DataSource;

public class Step070 {
    private static final String[] specialChar = {"<", ">", "&", "'", "\""};
    private Log log = LogFactory.getLog(Step070.class);
    private DataSource ds;
    private String queueName;
    private JmsTemplate jmsTemplate;

    public void onSignal() {
        String msg = getSelect();
        log.debug("==== message send ====");
        sendMessage(msg);
        log.debug("==== message end ====");
    }

    private String getSelect() {
        Connection conn = null;

        Statement stmt = null;
        ResultSet rs = null;
        String xmlMessage = null;
        try {
            conn = ds.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from testemp");

            String dbMsg = cvtRsToXml(rs, "row");

            xmlMessage = getSendMsg(dbMsg);

            log.debug("\n\n" + xmlMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch(SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return xmlMessage;
    }

    private String getSendMsg(String dbmsq) {
        String msg = "<?xml version=\"1.0\"?><root><header><time>"
                + System.currentTimeMillis() + "<time></header><body>" + dbmsq
                + "</body></root>";
        return msg;
    }

    public static String cvtRsToXml(ResultSet rs, String tagName) throws SQLException {
        StringBuffer result = new StringBuffer();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            while(rs.next()) {
                String colName = null;
                result.append("<" + tagName + ">\n");
                for(int i = 1; i <= rsmd.getColumnCount(); i++) {
                    colName = rsmd.getColumnName(i);
                    if(rsmd.getColumnType(i) == Types.CLOB) {

                    } else if (rsmd.getColumnType(i) == Types.BLOB) {

                    } else if (rsmd.getColumnType(i) == Types.LONGVARCHAR){

                    } else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {

                    } else {
                        result.append("\t" + colName + ">");
                        result.append(getCdataFormat(checkString(rs.getString(colName))));
                        result.append("<" + colName + ">\n");
                    }
                }
                result.append("</" + tagName + ">");
            }

        }finally {

        }
        return result.toString();
    }

    public static String getCdataFormat(String data) {
        if(data == null || "".equals(data)) return data;

        for(int i = 0; i < specialChar.length; i++) {
            if(data.indexOf(specialChar[i]) != -1) {
                return "<![CDATA[" + data + "]]>";
            }
        }
        return data;
    }

    public static String checkString(String str) {return checkString(str, "");}
    public static String checkString(String str, String tmp) {
        if(!(str == null || str.trim().equals("") || str.trim().equals("null")))
            return (String)str.trim().toString();
        else return tmp;
    }

    public DataSource getDs() {return ds;}
    public void setDs(DataSource ds) {this.ds = ds;}

    private void sendMessage(String msg) {
        jmsTemplate.convertAndSend(getQueueName(), msg, new MessagePostProcessor() {
            public Message postProcessMessage(Message arg0) throws JMSException {
                arg0.setStringProperty("txid", "1234");
                return arg0;
            }
        });
    }

    public String getQueueName() {return queueName;}
    public void setQueueName(String queueName) {this.queueName = queueName;}
    public JmsTemplate getJmsTemplate() {return jmsTemplate;}
    public void setJmsTemplate(JmsTemplate jmsTemplate) {this.jmsTemplate = jmsTemplate;}
}