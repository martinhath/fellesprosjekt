package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.util.logging.Logger;

public class CalendarController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private Button meetingButton;

    @FXML
    public void newMeeting(ActionEvent a) {
        String path = Layout.NewMeeting;
        getApplication().newScene(path);
    }

    public void logout(ActionEvent actionEvent) {
        Request req = new AuthRequest(Request.Type.POST,
                AuthRequest.Action.LOGOUT, null);
        getClient().sendTCP(req);

        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.OK){
                    Platform.runLater(() -> {
                        getApplication().setScene(getStage(), Layout.Login);
                    });
                } else {
                    logger.warning((String) res.payload);
                }
                getClient().removeListener(this);
            }
        });
    }
}
