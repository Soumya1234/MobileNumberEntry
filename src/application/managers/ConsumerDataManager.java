package application.managers;

import java.sql.Connection;
import java.time.LocalDate;

import application.daos.ConsumerDataDAO;
import application.entities.ConsumerData;

public class ConsumerDataManager {
	private ConsumerDataDAO dao;
	
	private static ConsumerDataManager instance;
	
	private ConsumerDataManager(Connection con) {
		dao = new ConsumerDataDAO(con);
	}
	
	public static ConsumerDataManager getInstance(Connection con) {
		if(instance == null) {
			instance = new ConsumerDataManager(con);
		}
		return instance;
	}
	
	public ConsumerData getConsumerData(int id) {
		return dao.getConsumerData(id);
	}
	
	public boolean updateConsumerData(int id, String mobile_no,String client_ip, LocalDate date) {
		return dao.updateConsumerData(id, mobile_no,client_ip,date);
	}
}
