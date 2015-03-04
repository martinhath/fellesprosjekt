package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.Listener;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import org.fellesprosjekt.gruppe24.client.CalendarClient;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.LoginRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends ClientController {

    private Client client;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label status_txt;

    @FXML
    public void loginClick(ActionEvent e){
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        LoginInfo loginInfo = new LoginInfo(username, password);

        LoginRequest req = new LoginRequest(Request.Type.POST, loginInfo);

        client.sendTCP(req);

        client.addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
            	Platform.runLater(() -> {
            		if (res.type == Response.Type.OK) {
            			handleLogin();
            		} else {
            			handleRejectedLogin(res);
            		}
            	});
                client.removeListener(this);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = CalendarClient.GetInstance().getClient();
    }

    public void handleLogin(){
        System.out.println("Vi er nå logget inn");
        getApplication().setScene("/layout/Kalender.fxml");
    }

    public void handleRejectedLogin(Response res){
    	System.err.println(res.payload);
        status_txt.setText((String) res.payload);
    }

}
