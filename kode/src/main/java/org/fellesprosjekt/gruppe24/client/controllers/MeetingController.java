package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MeetingController extends ClientController {
    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML
    private TextField fieldRoom;
    @FXML
    private TextField fieldFromTime;
    @FXML
    private TextField fieldToTime;
    @FXML
    private ComboBox dropdownParticipants;
    @FXML
    private DatePicker datePicker;

    @FXML
    private Button buttonOk;
    @FXML
    private Button buttonAbort;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        init();
    }

    private Meeting meeting;

    private void init() {
        /*
        Gjør det slik at validateASD() funksjonene kjøres
        hver gang fokus for tekstboksene (ol.) endres.
         */
        fieldRoom.focusedProperty().addListener(
                (FocusChangeListener) -> validateRoom());
        fieldFromTime.focusedProperty().addListener(
                (FocusChangeListener) -> validateFromTime());
        fieldToTime.focusedProperty().addListener(
                (FocusChangeListener) -> validateToTime());

        meeting = new Meeting();
    }


    /**
     * == VALIDERING ==
     * Dersom et felt ikke er obligatorisk (er det noen)
     * så må validate returnere `true` hvis det er tomt.
     */

    /**
     * Håndterer validaring av tekstfeltet for Room.
     * @return `true` om tekstfeltet er gyldig.
     */
    private boolean validateRoom() {
        return true;
    }

    private boolean validateDate() {
        return true;
    }

    private boolean validateFromTime() {
        System.out.println("validate from time");
        return true;
    }
    private boolean validateToTime() {
        return true;
    }

    private boolean validateParticipants() {
        return true;
    }

    private boolean validateFields() {
        return validateRoom() && validateDate() &&
                validateFromTime() && validateToTime() &&
                validateParticipants();
    }

    public void clickOk(ActionEvent actionEvent) {
        if (!validateFields())
            return;

        Request req = new MeetingRequest(Request.Type.POST, meeting);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL){
                    logger.info((String) res.payload);
                    // vis noe på skjermen om at det skjedde en feil
                    return;
                }
                getApplication().removeStage(getStage());
                getClient().removeListener(this);
            }
        });
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: Kanskje legge inn en 'er du sikker?' hvis vi har noe data
        getApplication().removeStage(getStage());
    }
}
