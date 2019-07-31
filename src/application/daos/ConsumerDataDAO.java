package application.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import application.entities.ConsumerData;
import application.util.Messages;

public class ConsumerDataDAO {
	
	private Connection con;

	public ConsumerDataDAO(Connection con) {
		this.con = con;
	}

	/*
	 * Returns the Consumer Data corresponding to the id argument. Returns null if
	 * no record found
	 */
	public ConsumerData getConsumerData(int id) {
	    ConsumerData cdata = null;
		try {
			String query = "SELECT * FROM MOBNOENTRY.CONSUMER_DATA WHERE CONTRACT_ACCOUNT=?";
			PreparedStatement pst = con.prepareStatement(query);
			pst.setString(1, Integer.toString(id));
			ResultSet rst=pst.executeQuery();
			if(rst.next()) {
				String name = rst.getString("NAME");
				String mobile_no = rst.getString("MOBILE_NUMBER");
				cdata = new ConsumerData(id,name,mobile_no,null,null);
			}
		} catch (SQLException e) {
			Messages.ShowErrorMessage(e.getMessage(), "Error");
		}
		return cdata;
	}
	
	/*
	 * Returns true after successful update of the mobile no
	 */
	public boolean updateConsumerData(int id, String mobile_no,String client_ip, LocalDate date) {
		// code to update mobile no against the input id
		try {
		String query = "UPDATE MOBNOENTRY.CONSUMER_DATA SET MOBILE_NUMBER = ? ,CLIENT_IP = ? ,MOBNO_UPDATE_DATE =? WHERE CONTRACT_ACCOUNT=?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,mobile_no);
		pst.setString(2,client_ip);
		pst.setDate(3,Date.valueOf(date));
		pst.setString(4,Integer.toString(id));
		pst.execute();
		return true;
		}
		catch(SQLException e) {
			Messages.ShowErrorMessage(e.getMessage(), "Error");
		}
		return false;
	}

}
