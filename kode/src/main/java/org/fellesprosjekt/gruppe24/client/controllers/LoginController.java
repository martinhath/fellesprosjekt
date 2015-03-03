package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.fellesprosjekt.gruppe24.client.CalendarClient;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
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

    public void loginClick(ActionEvent e){
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        LoginInfo loginInfo = new LoginInfo(username, password);

        Request req = new Request(Request.Type.AUTH, User.class, loginInfo);

        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection conn, Object obj) {
                if (!(obj instanceof Response)) {
                    System.err.println("Response error: " + obj);
                    return;
                }
                Response res = (Response) obj;

                if (res.getType() == Response.Type.SUCCESS) {
                    handleLogin();
                } else {
                    handleRejectedLogin();
                }
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

    public void handleRejectedLogin(){
        System.out.println("Vi er ikke logget inn :(");
    }

}
