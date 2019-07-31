package application.entities;

import java.time.LocalDate;
import java.util.Date;

public class ConsumerData {
	private int id;
	private String name;
	private String mobile_no;
	private String client_ip;
	private LocalDate update_date;
	
	public ConsumerData(int id, String name, String mobile_no, String client_ip, LocalDate update_date) {
		super();
		this.id = id;
		this.name = name;
		this.mobile_no = mobile_no;
		this.client_ip = client_ip;
		this.update_date = update_date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public LocalDate getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(LocalDate update_date) {
		this.update_date = update_date;
	}
}


