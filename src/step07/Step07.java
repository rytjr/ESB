package step07;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;


public class Step07 {
	private static final String[] specialChar = {"<", ">", "&", "'", "\""};
	private Log log = LogFactory.getLog(Step07.class);
	private DataSource ds;
	private String queueName;
    private JmsTemplate jmsTemplate;

	public void onSignal(){
        String msg = getSelect();
        log.debug("===== message send =====");
        sendMessage(msg);
        log.debug("===== message end =====");
    }

	private String getSelect() {
		Connection conn = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		String xmlMessage=null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from testemp ");
//			rs = stmt.executeQuery("select * from testemp where name='james1'");
			
			//����Ʈ�� rs �� ��� ���ڵ� �� ���� <row></row>�� ���� xml��
			String dbMsg = cvtRsToXml(rs, "row");
			
			xmlMessage = getSendMsg(dbMsg);
			
			log.debug("\n\n"+xmlMessage);
			
//			while (rs.next()) {
//				log.debug(rs.getString(1));
//			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
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
			
			if(conn !=null) {
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
		String msg = "<?xml version=\"1.0\"?><root><header><time>"
				+ System.currentTimeMillis() + "<time></header><body>" + dbmsg
				+ "</body></root>";
		return msg;
	}

	/**
	 * ResultSet�� XML �� ��ȯ
	 * 
	 * @param rs
	 * @param tagName
	 * @return
	 * @throws SQLException
	 */
	public static String cvtRsToXml(ResultSet rs, String tagName)
			throws SQLException {
		StringBuffer result = new StringBuffer();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				String colName = null;
				result.append("<" + tagName + ">\n"); // start tag
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					colName = rsmd.getColumnName(i);
					if (rsmd.getColumnType(i) == Types.CLOB) {
					} else if (rsmd.getColumnType(i) == Types.BLOB) {
					} else if (rsmd.getColumnType(i) == Types.LONGVARCHAR) {
					} else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
					} else {
						result.append("\t<" + colName + ">");
						result.append(getCdataFormat(checkString(rs
										.getString(colName))));
						result.append("</" + colName + ">\n");
					}
				}
				result.append("</" + tagName + ">"); // end tag
			}
		} finally {

		}

		return result.toString();
	}
	
	  /**
     * Ư�����ڰ� ������ CDATA wrapping
     * @param data
     * @return Ư������ ������ ���� ��� String
     */
    public static String getCdataFormat(String data){
        if( data == null || "".equals(data))  return data;
        
        for( int i = 0 ; i < specialChar.length ; i++ ) {
            if( data.indexOf( specialChar[i] ) != -1 ) {  // Ư�����ڰ�  ������ ![CDATA[]]ó��
                return "<![CDATA[" + data + "]]>";
            }
        }
        
        return data;
    }

    /**
     * null check method ( null �� �ƴ� ��� �յ� �������� ���ڿ� ��ȯ, null �� ��� "" ��ȯ)
     * @param str null check �� ���ڿ�
     * @return String
     */
    public static String checkString(String str)
    {
        return checkString(str, "");
    }
    public static String checkString(String str, String tmp)
    {
        if (!(str == null || str.trim().equals("")|| str.trim().equals("null")))
            return (String)str.trim().toString();
        else    return tmp;
    }
    
	// bean properteis...
	public DataSource getDs() {
		return ds;
	}

	public void setDs(DataSource ds) {
		this.ds = ds;
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
