package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.fellesprosjekt.gruppe24.common.models.Notification;
import org.fellesprosjekt.gruppe24.common.models.net.NotificationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class NotificationController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private List<Notification> notifications = new LinkedList<>();
    private Notification notification;

    @FXML ListView<Label> listView;
    @FXML Button abortButton;

    @FXML public Button buttonAccept;
    @FXML public Button buttonDeny;

    private boolean showRead = true;

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
                    if (i == -1)
                        return;
                    notification = notifications.get(i);
                    if (!notification.isRead())
                        readNotification(notification);
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
        listView.getItems().clear();
        notifications.clear();
        for (Notification n : CalendarController.notifications) {
            if (showRead || !n.isRead())
                addNotificationToList(n);
        }
    }

    private void addNotificationToList(Notification not) {
        notifications.add(not);
        Label label = new Label();
        label.setText(not.getMessage());
        if (not.isRead())
            label.setStyle("-fx-text-fill: #aaa");
        listView.getItems().add(label);
    }

    public void toggleShowRead() {
        showRead = !showRead;
        init();
    }

    public void clickAbort(ActionEvent actionEvent) {
        getApplication().removeStage(getStage());
    }

    private void readNotification(Notification not) {
        not.setRead(true);
        Request req = new NotificationRequest(Request.Type.PUT, not);
        getClient().sendTCP(req);
        int i = notifications.indexOf(not);
        listView.getItems().get(i).setStyle("-fx-text-fill: #aaa");
    }

    public void deny(ActionEvent actionEvent) {
        int index = notifications.indexOf(notification);
        listView.getItems().remove(index);
        notifications.remove(index);

        notification.setConfirmed(false);
        notification.setRead(true);
        Request req = new NotificationRequest(Request.Type.PUT, notification);
        getClient().sendTCP(req);

        if (index == notifications.size())
            index--;
        if (index > 0)
            notification = notifications.get(index);
        else
            notification = null;
    }

    public void accept(ActionEvent actionEvent) {
        int index = notifications.indexOf(notification);
        listView.getItems().remove(index);
        notifications.remove(index);

        notification.setConfirmed(true);
        notification.setRead(true);
        Request req = new NotificationRequest(Request.Type.PUT, notification);
        getClient().sendTCP(req);

        if (index == notifications.size())
            index--;
        if (index > 0)
            notification = notifications.get(index);
        else
            notification = null;
    }

}
