package step06;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Step06 {
	private static final String[] specialChar = {"<", ">", "&", "'", "\""};
	private Log log = LogFactory.getLog(Step06.class);
	private DataSource ds;

	public void init() {
		getSelect();
	}

	private void getSelect() {
		Connection conn = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from testemp");
//			rs = stmt.executeQuery("select * from testemp where name='james1'");
			
			//리절트셋 rs 에 담긴 레코드 각 건을 <row></row>로 묶기
			String dbMsg = cvtRsToXml(rs, "row");
			//<header><body><root>등의 태그를 추가하여 표준 xml로 만들기
			String xmlMessage = getSendMsg(dbMsg);
			
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
		
	}

	private String getSendMsg(String dbmsg) {
		String msg = "<?xml version=\"1.0\"?><root><header><time>"
				+ System.currentTimeMillis() + "<time></header><body>" + dbmsg
				+ "</body></root>";
		return msg;
	}

	/**
	 * ResultSet을 XML 로 반환
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
     * 특수문자가 있으면 CDATA wrapping
     * @param data
     * @return 특수문자 유무에 따른 결과 String
     */
    public static String getCdataFormat(String data){
        if( data == null || "".equals(data))  return data;
        
        for( int i = 0 ; i < specialChar.length ; i++ ) {
            if( data.indexOf( specialChar[i] ) != -1 ) {  // 특수문자가  있으면 ![CDATA[]]처리
                return "<![CDATA[" + data + "]]>";
            }
        }
        
        return data;
    }

    /**
     * null check method ( null 이 아닐 경우 앞뒤 공백제거 문자열 반환, null 일 경우 "" 반환)
     * @param str null check 할 문자열
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

}
