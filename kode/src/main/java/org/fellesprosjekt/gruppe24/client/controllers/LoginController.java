package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.fellesprosjekt.gruppe24.client.CalendarClient;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    private Client client;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    public void loginClick(ActionEvent e){
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        LoginInfo loginInfo = new LoginInfo(username, password);

        Request req = new Request(Request.Type.GET, User.class, loginInfo);

        client.sendTCP(req);

        System.out.println("Trykk!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = CalendarClient.GetInstance().getClient();
    }

}
