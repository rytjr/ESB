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
			
			//리절트셋 rs 에 담긴 레코드 각 건을 <row></row>로 묶기
			String dbMsg = cvtRsToXml(rs, "row");
			
			log.debug("\n\n"+dbMsg);

			// 저장 경로와 파일 이름 지정
			String directory = "C:/edu/file"; // 저장할 디렉터리 경로
			String fileName = "20250107.txt"; // 저장할 파일 이름
			String content = dbMsg;

			try {
				// 경로 생성 (디렉터리가 없으면 생성)
				Path path = Paths.get(directory);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
					System.out.println("Directory created: " + path);
				}

				// 파일 저장
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
