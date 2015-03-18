package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.Notification;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.NotificationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import com.esotericsoftware.kryonet.Connection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class NotificationController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());
    private Notification not;

    @FXML
    ListView<Notification> listView;
    @FXML
    Button abortButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void init() {

        // får notifications fra server
        Request req = new NotificationRequest();
        req.type = Request.Type.LIST;
        req.payload = not;
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    logger.info((String) res.payload);
                    // vis noe på skjermen om at det skjedde en feil
                    getClient().removeListener(this);
                    return;
                }
                if (!listInstanceOf(res.payload, Notification.class)){
                    getClient().removeListener(this);
                    return;
                }
                List<Notification> list = (List<Notification>) res.payload;
                // alt var good.
                System.out.println("good");
                System.out.println(list);
                listView.getItems().addAll(list);
                getClient().removeListener(this);
            }
        });
    }

    private void addNotificationToList(Notification not) {
        Label label = new Label();
        label.setStyle("-fx-font-color: #ff9933");
        listView.getItems().add(not);
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: Kanskje legge inn en 'er du sikker?' hvis vi har noe data
        getApplication().removeStage(getStage());
    }

}

	

