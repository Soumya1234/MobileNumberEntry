package application.controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import application.entities.ConsumerData;
import application.exceptions.BlankFieldException;
import application.exceptions.InvalidInputException;
import application.managers.ConsumerDataManager;
import application.util.Configure;
import application.util.DBConnection;
import application.util.Messages;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class UserviewController implements Initializable {
	private Connection con;
	private boolean mobileNoExists = false;
	@FXML
	private Pane pane_root;
	@FXML
	private TextField consumer_id;
	@FXML
	private TextField mobile_no;
	@FXML
	private Button update_button;
	@FXML
	private Button reset_button;
	@FXML
	private Label consumer_name_label;
	@FXML
	private TextField server_ip;
	@FXML
	private TextField server_port;
	@FXML
	private Label message_label;

	/*
	 * Code to respond to CTRL+R to reset the fields
	 */
	@FXML
	public void handleOnRKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.R) {
			resetFields();
			keyEvent.consume();
		}
	}

	/*
	 * Positions the cursor at the beginning of the text fields when clicked on them
	 */
	@FXML
	public void handleTextFieldActionEvent(ActionEvent e) {
		consumer_id.positionCaret(0);
		mobile_no.positionCaret(0);
	}

	/*
	 * Event handler for the Update button
	 */
	@FXML
	public void handleUpdateButtonEvent(ActionEvent e) {
		updateMobileNo();

	}

	/*
	 * Event handler for the Reset button
	 */
	@FXML
	public void handleResetButtonEvent(ActionEvent e) {
		resetFields();

	}

	@FXML
	public void handleSaveConfigurationButtonEvent(ActionEvent e) {
		try {
			String server_ip_address = server_ip.getText();
			String server_port_number = server_port.getText();
			if (server_ip_address.length() == 0 || server_port_number.length() == 0) {
				throw new BlankFieldException("Please Enter Data Server Configuration");
			}
			Configure.saveConfiguration(server_ip_address, server_port_number);
			Messages.ShowInfoMessage("Configuration Saved Successfully", "Success");
		} catch (BlankFieldException ex) {
			Messages.ShowErrorMessage(ex.getMessage(), "Error");
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		/*
		 * Connecting to the database at the start of the application. Also populating
		 * the server_ip and server_port_numbers at the Configure App tab of the UI
		 */
		try {
			server_ip.setText(Configure.readIP());
			server_port.setText(Configure.readPort());
			DBConnection.connectToDatabase();
			con = DBConnection.getConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Messages.ShowErrorMessage(e1.getMessage(), "Error");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			Messages.ShowErrorMessage(e1.getMessage(), "Error");
		}

		/*
		 * The app starts with empty Consumer Name label, Mobile No textfield and Update
		 * Button disabled
		 */

		consumer_name_label.setText("");
		mobile_no.setEditable(false);
		update_button.setDisable(true);
		message_label.setText("");

		/*
		 * Event handler for the Consumer ID textfield. Quering the database to check
		 * whether the Consumer ID and corresponding mobile no exists goes into this
		 * event handler
		 */
		consumer_id.textProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("unused")
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				try {
					System.out.println(arg2.length());
					if (arg2.length() == 9) {
						if (arg2.matches("[0-9]+")) {

							// code to query the database
							ConsumerDataManager manager = ConsumerDataManager.getInstance(con);
							ConsumerData cdata = manager.getConsumerData(Integer.parseInt(arg2));
							if (cdata == null) {
								message_label.setTextFill(Color.web("#ff0000", 0.8));
								message_label.setText("*No Consumer Found");
							} else {
								consumer_name_label.setText(cdata.getName());
								message_label.setText("");
								// System.out.println("The mobile no length is:
								// "+cdata.getMobile_no().length());
								if (cdata.getMobile_no() == null || cdata.getMobile_no().length() < 10) {
									mobile_no.setEditable(true);
									mobile_no.requestFocus();
									mobile_no.positionCaret(0);
									update_button.setDisable(false);
								} else {
									mobile_no.setText(cdata.getMobile_no());
									mobile_no.setEditable(false);
									mobileNoExists =true;
								}
							}

						} else {
							throw new InvalidInputException("Please enter valid Consumer ID");
						}
					} else {
						consumer_name_label.setText("");
						update_button.setDisable(true);
						// message_label.setText(" ");
						mobile_no.setText("");
						mobile_no.setEditable(false);
					}
				} catch (InvalidInputException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println("Exception generated:" + e.getMessage());
				}
			}
		});

		/*
		 * Code to handle ENTER key press after entering mobile no in the relevant text
		 * field
		 */
		mobile_no.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					if (mobileNoExists) {
						message_label.setTextFill(Color.web("#ff0000", 0.8));
						message_label.setText("Mob. No. already exists");
					} else {
						updateMobileNo();
					}
					keyEvent.consume();
				}
			}
		});

	}

	
	/*
	 * Method to update the mobile number in database
	 */
	private void updateMobileNo() {
		System.out.println("mobile_no.getText().length():"+mobile_no.getText().length());
		try {
			if (mobile_no.getText().length() != 10 || !mobile_no.getText().matches("[0-9]+")) {
				throw new InvalidInputException("Please enter valid 10 digit mobile number");
			}
			String mobno = mobile_no.getText();
			int id = Integer.parseInt(consumer_id.getText());
			LocalDate now = LocalDate.now();
			InetAddress localhost = InetAddress.getLocalHost();
			String client_ip = (localhost.getHostAddress()).trim();
			ConsumerDataManager manager = ConsumerDataManager.getInstance(con);
			if (manager.updateConsumerData(id, mobno, client_ip, now)) {
				message_label.setTextFill(Color.web("#00cc00", 0.8));
				message_label.setText("Successfully updated");
				consumer_id.setText("");
				mobile_no.setText("");
				consumer_id.requestFocus();
				update_button.setDisable(true);
			}
		} catch (InvalidInputException e1) {
			Messages.ShowErrorMessage(e1.getMessage(), "Error");
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			Messages.ShowErrorMessage(e1.getMessage(), "Error");
		}
	}

	/*
	 * Method to reset the fields and labels
	 */
	private void resetFields() {
		consumer_id.setText("");
		consumer_id.requestFocus();
		message_label.setText("");
		mobile_no.setText("");
		update_button.setDisable(true);
		mobileNoExists =false;
	}

}
