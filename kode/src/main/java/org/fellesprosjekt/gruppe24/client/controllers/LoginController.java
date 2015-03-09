package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

public class LoginController extends ClientController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label status_txt;

    @FXML
    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            loginClick(null);
    }

    @FXML
    public void loginClick(ActionEvent e){
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        LoginInfo loginInfo = new LoginInfo(username, password);


        AuthRequest req = new AuthRequest(Request.Type.POST,
                AuthRequest.Action.LOGIN, loginInfo);

        Client client = getClient();
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

    public void handleLogin(){
        System.out.println("Vi er nå logget inn");
        ClientController controller = getApplication()
                .newScene(Layout.Calendar);
        getApplication().removeStage(getStage());
    }

    public void handleRejectedLogin(Response res){
    	System.err.println(res.payload);
        status_txt.setText((String) res.payload);
    }
}
