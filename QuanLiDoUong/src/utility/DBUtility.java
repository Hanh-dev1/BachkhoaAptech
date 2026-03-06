package utility;

import java.sql.*;

public class DBUtility {
	public static Connection openConnection() {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS01;databaseName=QuanLiDoUong","sa","1234$");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
