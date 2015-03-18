package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

import java.net.URL;
import java.util.ResourceBundle;

public class MeetingDetailController extends ClientController {

    private Meeting meeting;

    private boolean isOwner = false;
    private boolean editMode = false;

    @FXML public Label labelTitle;
    @FXML public TextArea textDesc;
    @FXML public ComboBox<Entity> comboOwner;
    @FXML public TextField textRoom;
    @FXML public DatePicker datePicker;
    @FXML public TextField textFrom;
    @FXML public TextField textTo;

    @FXML public ListView<Entity> listParticipants;
    @FXML public ListView<Entity> listInvited;

    @FXML public Button buttonEdit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    private void checkAdmin() {
        if (getApplication().getUser().getId() == meeting.getOwner().getId()){
            isOwner = true;
            buttonEdit.setVisible(true);
        } else {
            buttonEdit.setVisible(false);
        }
    }

    public void setMeeting(Meeting m) {
        meeting = m;
        checkAdmin();
        setFields();
    }

    private void setFields() {
        textDesc.setDisable(!editMode);
        comboOwner.setDisable(!editMode);
        textRoom.setDisable(!editMode);
        datePicker.setDisable(!editMode);
        textFrom.setDisable(!editMode);
        textTo.setDisable(!editMode);

        if (meeting == null)
            return;

        comboOwner.getItems().add(meeting.getOwner());
        comboOwner.getSelectionModel().select(meeting.getOwner());
        labelTitle.setText(meeting.getName());
        textDesc.setText(meeting.getDescription());
        //textRoom.setText(meeting.getRoom().getName());
        datePicker.setValue(meeting.getFrom().toLocalDate());
        textFrom.setText(meeting.getFrom().format(Formatters.hhmmformat));
        textTo.setText(meeting.getTo().format(Formatters.hhmmformat));

        for (Entity e: meeting.getParticipants()){
            listParticipants.getItems().add(e);
        }
    }


    private boolean validateFields() {
        // TODO: denne.
        // bør vise hvilke felter som er gale.
        return true;
    }

    public void clickEdit(ActionEvent actionEvent) {
        if (!validateFields())
            return;

        if (editMode){
            editMode = false;
            buttonEdit.setText("Rediger");
        } else {
            editMode = true;
            buttonEdit.setText("Ferdig");
        }
        setFields();
    }

    public void clickBack(ActionEvent actionEvent) {
        getApplication().setScene(getStage(), Layout.Calendar);
    }
}
