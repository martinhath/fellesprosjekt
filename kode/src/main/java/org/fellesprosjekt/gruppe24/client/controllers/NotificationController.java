package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.application.Platform;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.GroupNotification;
import org.fellesprosjekt.gruppe24.common.models.MeetingNotification;
import org.fellesprosjekt.gruppe24.common.models.Notification;
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

    private List<Notification> notifications = new LinkedList<>();

    @FXML ListView<Label> listView;
    @FXML Button abortButton;

    @FXML public Button buttonAccept;
    @FXML public Button buttonDeny;

    private Notification notification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        hideButtons();

        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldVal, newVal) -> {
                    if (newVal == null)
                        hideButtons();
                    else
                        showButtons();

                    int i = listView.getItems().indexOf(newVal);
                    notification = notifications.get(i);
                });
    }

    private void showButtons() {
        buttonAccept.setVisible(true);
        buttonDeny.setVisible(true);
    }

    private void hideButtons() {
        buttonAccept.setVisible(false);
        buttonDeny.setVisible(false);
    }

    public void init() {
        // får notifications fra server
        NotificationRequest req = new NotificationRequest(Request.Type.LIST,
                true, NotificationRequest.Handler.BOTH, getApplication().getUser());
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    logger.info((String) res.payload);
                    return;
                }
                if (!listInstanceOf(res.payload, MeetingNotification.class) &&
                        !listInstanceOf(res.payload, GroupNotification.class)){
                    return;
                }
                List<Notification> list = (List<Notification>) res.payload;
                Platform.runLater(() -> {
                    listView.getItems().clear();
                    notifications.clear();
                    for (Notification n : list) {
                        addNotificationToList(n);
                    }
                });
                getClient().removeListener(this);
            }
        });
    }

    private void addNotificationToList(Notification not) {
        notifications.add(not);
        Label label = new Label();
        label.setText(not.getMessage());
        listView.getItems().add(label);
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: Kanskje legge inn en 'er du sikker?' hvis vi har noe data
        getApplication().removeStage(getStage());
    }

    public void deny(ActionEvent actionEvent) {
        System.out.println("Oker ikke " + notification.getMessage());
    }

    public void accept(ActionEvent actionEvent) {
        System.out.println("Skal lett på " + notification.getMessage());
    }
}

	

