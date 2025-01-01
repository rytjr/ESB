package step08_snd;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;

public class Step081_snd {
    private static final String[] specialChar = new String[]{"<", ">", "&", "'", "\""};
    private static Log log = LogFactory.getLog(Step081_snd.class);
    private DataSource ds;
    private String queueName;
    private JmsTemplate jmsTemplate;

    public Step081_snd() {
    }

    public void onSignal() {
        String msg = this.getSelect();
        log.debug("===== message send =====");
        this.sendMessage(msg);
        log.debug("===== message end =====");
    }

    private String getSelect() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String xmlMessage = null;
        String test = "";

        try {
            conn = this.ds.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from testemp ");
            String dbMsg = cvtRsToXml(rs, "row");
            xmlMessage = this.getSendMsg(dbMsg);
            log.debug("\n\n" + xmlMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
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

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        return xmlMessage;
    }

    private String getSendMsg(String dbmsg) {
        String msg = "<?xml version=\"1.0\"?><root><header><time>" + System.currentTimeMillis() + "<time></header><body>" + dbmsg + "</body></root>";
        return msg;
    }

    public static String cvtRsToXml(ResultSet rs, String tagName) throws SQLException {
        StringBuffer result = new StringBuffer();
        ResultSetMetaData rsmd = rs.getMetaData();

        while(rs.next()) {
            String colName = null;
            result.append("<" + tagName + ">\n");

            for(int i = 1; i <= rsmd.getColumnCount(); ++i) {
                colName = rsmd.getColumnName(i);
                if (rsmd.getColumnType(i) != 2005 && rsmd.getColumnType(i) != 2004 && rsmd.getColumnType(i) != -1 && rsmd.getColumnType(i) != -4) {
                    result.append("\t<" + colName + ">");
                    result.append(getCdataFormat(checkString(rs.getString(colName))));
                    result.append("</" + colName + ">\n");
                }
            }

            result.append("</" + tagName + ">");
        }

        return result.toString();
    }

    public static String getCdataFormat(String data) {
        if (data != null && !"".equals(data)) {
            for(int i = 0; i < specialChar.length; ++i) {
                if (data.indexOf(specialChar[i]) != -1) {
                    return "<![CDATA[" + data + "]]>";
                }
            }

            return data;
        } else {
            return data;
        }
    }

    public static String checkString(String str) {
        return checkString(str, "");
    }

    public static String checkString(String str, String tmp) {
        return str != null && !str.trim().equals("") && !str.trim().equals("null") ? str.trim().toString() : tmp;
    }

    public DataSource getDs() {
        return this.ds;
    }

    public void setDs(DataSource ds) {
        this.ds = ds;
    }

    private void sendMessage(String msg) {
        this.jmsTemplate.convertAndSend(this.getQueueName(), msg, new MessagePostProcessor() {
            public Message postProcessMessage(Message arg0) throws JMSException {
                arg0.setStringProperty("txid", "1234");
                return arg0;
            }
        });
    }

    public String getQueueName() {
        return this.queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public JmsTemplate getJmsTemplate() {
        return this.jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
