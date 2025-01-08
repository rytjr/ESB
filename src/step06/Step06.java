package step06;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
			rs = stmt.executeQuery("select * from RCV_CRW");
//			rs = stmt.executeQuery("select * from testemp where name='james1'");
			
			//����Ʈ�� rs �� ��� ���ڵ� �� ���� <row></row>�� ����
			String dbMsg = cvtRsToXml(rs, "row");
			
			log.debug("\n\n"+dbMsg);

			// ���� ��ο� ���� �̸� ����
			String directory = "C:/edu/file"; // ������ ���͸� ���
			String fileName = "20250107.txt"; // ������ ���� �̸�
			String content = dbMsg;

			try {
				// ��� ���� (���͸��� ������ ����)
				Path path = Paths.get(directory);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
					System.out.println("Directory created: " + path);
				}

				// ���� ����
				String filePath = directory + "/" + fileName;
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
					writer.write(content);
					writer.newLine();
					System.out.println("File saved successfully at: " + filePath);
				}

			} catch (IOException e) {
				System.out.println("An error occurred while writing the file.");
				e.printStackTrace();
			}
			
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
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					colName = rsmd.getColumnName(i);
					if (rsmd.getColumnType(i) == Types.CLOB) {
					} else if (rsmd.getColumnType(i) == Types.BLOB) {
					} else if (rsmd.getColumnType(i) == Types.LONGVARCHAR) {
					} else if (rsmd.getColumnType(i) == Types.LONGVARBINARY) {
					} else {
						result.append(colName + " : ");
						result.append(getCdataFormat(checkString(rs
										.getString(colName))));
						if(!colName.equals("URL")) {
							result.append(" | ");
						}

					}
				}
				result.append("\n");
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

}
