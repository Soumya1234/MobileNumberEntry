package application.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static Connection connection;

	public static void connectToDatabase() throws IOException,SQLException{
		try {
		String Server_IP = Configure.readIP();
		String Server_Port = Configure.readPort();
		Connection con=DriverManager.getConnection("jdbc:mysql://"+Server_IP+":"+Server_Port+"/mobnoentry", "appuser", "apppassword");
		connection = con;
		}
		catch(IOException e) {
			throw e;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
}

