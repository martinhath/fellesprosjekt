package org.fellesprosjekt.gruppe24.client.controllers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;

public class NewUserController extends ClientController {
	
	@FXML
	private TextField usernameField;
	
	@FXML
	private TextField passwordField;
	
	@FXML
	private TextField passwordField2;
	
	@FXML
	private TextField firstName;
	
	@FXML
	private TextField lastName;
	
	@FXML
	private TextField emailField;
	
	@FXML
	private Label status_txt;
	
	@FXML
	public void keyReleased(KeyEvent e) {
		status_txt.setTextFill(Paint.valueOf("#CC0000"));
		if(((TextField) e.getSource()).getId().equals(usernameField.getId())) {
			if(!checkUsername(usernameField.getText()))
				return;
		}
		if(((TextField) e.getSource()).getId().equals(passwordField.getId())) {
			if(!checkPassword(passwordField.getText()))
				return;
		}
		if(((TextField) e.getSource()).getId().equals(passwordField2.getId())) {
			if(!checkPasswordMatch(passwordField2.getText()))
				return;
		}
		if(((TextField) e.getSource()).getId().equals(firstName.getId()) ||
				((TextField) e.getSource()).getId().equals(lastName.getId())) {
			if(!checkName(firstName.getText(), lastName.getText()))
				return;
		}
		if(((TextField) e.getSource()).getId().equals(emailField.getId())) {
			if(!checkEmail(emailField.getText()))
				return;
		}
		status_txt.setText("");
	}
	
	private boolean checkUsername(String username) {
		if(username.length() < 3) {
			status_txt.setText("Brukernavnet er for kort.");
			return false;
		}
		if(!username.matches("^[a-zA-Z0-9]+$")) {
			status_txt.setText("Brukernavnet inneholder ugyldige tegn.");
			return false;
		}
		UserRequest req = new UserRequest(Request.Type.GET, usernameField.getText());
		getClient().sendTCP(req);
		getClient().addListener(new ClientListener(){
            public void receivedResponse(Connection conn, Response res) {

                if (!(res.payload instanceof User)) return;
                if(res.payload != null) {
                	Platform.runLater(() -> {
                		status_txt.setText("Brukernavnet er allerede tatt!");
                	});
                }
                getClient().removeListener(this);
            }
        });
		return true;
	}
	
	private boolean checkPassword(String password) {
		if (password.length() < 3) {
			status_txt.setText("Passordet er for kort.");
			return false;
		}
		return true;
	}
	
	private boolean checkPasswordMatch(String password) {
		if(!password.equals(passwordField.getText())) {
			status_txt.setText("Passordene er ikke like.");
			return false;
		}
		return true;
	}
	
	private boolean checkName(String f, String l) {
		if(f.length() < 2 || l.length() < 2) {
			status_txt.setText("For- eller etternavn er for kort.");
			return false;
		}
		/*String name = f + " " + l;
		if(!name.matches("/^[a-zA-ZæøåÆØÅ ,.'-]+$/u")) {
			status_txt.setText("Navnet inneholder ugyldige tegn.");
			return false;
		}*/
		return true;
	}
	
	private boolean checkEmail(String email) {
		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			status_txt.setText("Emailen er ikke gyldig.");
			return false;
		}
		return true;
	}
	
	@FXML
	public void registerClick(ActionEvent e) {
		boolean b = checkUsername(usernameField.getText()) && 
				checkPassword(passwordField.getText()) &&
				checkPasswordMatch(passwordField2.getText()) &&
				checkName(firstName.getText(), lastName.getText()) &&
				checkEmail(emailField.getText());
		if(!b)
			return;
		String fullName = firstName.getText() + " " + lastName.getText();
		User user = new User(usernameField.getText(), fullName, passwordField.getText(), emailField.getText());
		UserRequest req = new UserRequest(Request.Type.PUT, user);
		getClient().sendTCP(req);
		getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if(res.type == Response.Type.FAIL) {
                	status_txt.setText(res.payload.toString());
                } else {
                	Platform.runLater(() -> {
                		status_txt.setTextFill(Paint.valueOf("#00CC00"));
                		clearFields();
                		status_txt.setText("Brukeren er nå registrert!");
                	});
                }
                getClient().removeListener(this);
            }
        });
	}
	
	private void clearFields() {
		usernameField.setText("");
		passwordField.setText("");
		passwordField2.setText("");
		firstName.setText("");
		lastName.setText("");
		emailField.setText("");
	}
	 
	@FXML
	public void cancelClick(ActionEvent e) {
		getApplication().setScene(getStage(), Layout.Login);
	}

}
