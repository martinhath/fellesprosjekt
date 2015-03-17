package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

public class MeetingDetailController extends ClientController {

    private Meeting meeting;

    @FXML
    public Label labelDesc;

    public void setMeeting(Meeting m) {
        meeting = m;
        setFields();
    }

    private void setFields() {
        if (meeting == null) return;

        labelDesc.setText(meeting.getDescription());
    }

    public void clickEdit(ActionEvent actionEvent) {

    }

    public void clickBack(ActionEvent actionEvent) {
        getApplication().setScene(getStage(), Layout.Calendar);
    }
}
