package step06;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

public class Step060 {

    private static final String[] specialChar = {"<", ">", "&", "'","\""};
    private static Log log = LogFactory.getLog(Step060.class);
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
//            rs = stmt.executeQuery("select * from testemp where name = 'james1'");
            log.debug("==== rs ====" + rs);

            String dbMsg = cvtRsToXml(rs, "row");
            String xmlMessage = getSendMsg(dbMsg);

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
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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
                    log.debug("=== colName ====" + colName);
                    if(rsmd.getColumnType(i) == Types.CLOB) {
                    } else if (rsmd.getColumnType(i) == Types.BLOB){
                    } else if (rsmd.getColumnType(i) == Types.LONGVARCHAR) {
                    } else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
                    } else {
                        result.append("\t<" + colName + ">");
                        result.append(getCdataFormat(checkString(rs.getString(colName))));
                        result.append("</" + colName + ">\n");
                    }
                }
            }
        } finally {

        }
        return result.toString();
    }

    public static String getCdataFormat(String data) {
        if(data == null || "".equals(data)) return data;

        for(int i = 0; i < specialChar.length; i++) {
            if(data.indexOf(specialChar[i]) != -1) {
                return "<![CDATA[" + data + "]>";
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
}
