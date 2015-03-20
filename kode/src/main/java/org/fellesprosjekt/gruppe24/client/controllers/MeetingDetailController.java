package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.Regexes;
import org.fellesprosjekt.gruppe24.common.models.*;
import org.fellesprosjekt.gruppe24.common.models.net.*;

import java.awt.event.FocusListener;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MeetingDetailController extends ClientController {
    private Logger logger = Logger.getLogger(getClass().getName());

    private Meeting meeting;

    private boolean isOwner = false;
    private boolean editMode = false;

    @FXML public Label labelTitle;
    @FXML public TextArea textDesc;
    @FXML public ComboBox<Entity> comboOwner;
    @FXML public ComboBox<Room> comboRoom;
    @FXML public DatePicker datePicker;
    @FXML public TextField textFrom;
    @FXML public TextField textTo;

    @FXML public ListView<Entity> listParticipants;
    @FXML public ListView<Entity> listInvited;
    @FXML public ComboBox<Entity> comboInvite;
    @FXML public ComboBox<Entity> comboRemove;

    @FXML public Label labelError;

    @FXML public Button buttonBack;
    @FXML public Button buttonEdit;
    @FXML public Button buttonSave;
    @FXML public Button buttonDelete;

    @FXML public HBox hboxAdd;
    @FXML public HBox hboxRemove;

    private LocalTime totime;
    private LocalTime fromtime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        textFrom.focusedProperty().addListener((observable, notfocus, focus) -> {
            if (focus) return;
            if (validateFromTime())
                setOKText(textFrom);
            else
                setErrText(textFrom);
        });

        textTo.focusedProperty().addListener((observable, notfocus, focus) -> {
            if (focus) return;
            if (validateToTime())
                setOKText(textTo);
            else
                setErrText(textTo);
        });

        comboInvite.valueProperty().addListener(
                (observable, t, curr) ->
                        Platform.runLater(() ->
                                inviteUser(curr)));
        comboRemove.valueProperty().addListener(
                (observable, t, curr) ->
                        Platform.runLater(() ->
                                removeUser(curr)));
    }

    private void getRooms() {
        Request groupReq = new RoomRequest(Request.Type.LIST, null);
        getClient().sendTCP(groupReq);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    logger.info((String) res.payload);
                    return;
                }
                if (!listInstanceOf(res.payload, Room.class)) return;

                Platform.runLater(() -> {
                    comboRoom.getItems().clear();
                    comboRoom.getItems().addAll((List<Room>) res.payload);
                    comboRoom.getSelectionModel().select(meeting.getRoom());
                });
                getClient().removeListener(this);
            }
        });
    }

    private void getUsers() {
        Request userReq = new UserRequest(Request.Type.LIST, null);
        getClient().sendTCP(userReq);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    logger.info((String) res.payload);
                    return;
                }
                if (!listInstanceOf(res.payload, User.class))
                    return;

                Platform.runLater(() -> {
                    List<User> users = (List<User>) res.payload;

                    comboOwner.getItems().clear();
                    comboOwner.getItems().addAll(users);
                    comboOwner.getSelectionModel().select(
                            meeting.getOwner()
                    );
                    comboInvite.getItems().clear();
                    comboInvite.getItems().addAll(users.stream()
                            .filter((usr) -> !listInvited.getItems().contains(usr))
                            .filter((usr) -> !listParticipants.getItems().contains(usr))
                            .sorted((u1, u2) -> u1.getName().compareTo(u2.getUsername()))
                            .collect(Collectors.toList()));
                });
                getClient().removeListener(this);
            }
        });
    }

    private void getParticipantsAndInvited() {
        Request req = new NotificationRequest(Request.Type.LIST, meeting);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) return;
                if (res.payload == null) return;
                if (!listInstanceOf(res.payload, MeetingNotification.class)) return;

                List<Notification> notifications = (List<Notification>) res.payload;

                Platform.runLater(() -> {
                    Iterator it = listInvited.getItems().iterator();
                    while (it.hasNext()){
                        User u = (User) it.next();
                        comboInvite.getItems().add(u);
                        comboRemove.getItems().remove(u);
                        it.remove();
                    }
                    it = listParticipants.getItems().iterator();
                    while (it.hasNext()){
                        User u = (User) it.next();
                        comboInvite.getItems().add(u);
                        comboRemove.getItems().remove(u);
                        it.remove();
                    }
                    for (Notification not : notifications) {
                        User u = not.getUser();
                        if (not.isConfirmed()) {
                            addConfirmed(u);
                        } else if (!not.isRead()) {
                            addInvited(u);
                        }
                    }
                });
                getClient().removeListener(this);
            }
        });
    }

    private void checkAdmin() {
        if (getApplication().getUser().getId() == meeting.getOwner().getId()){
            isOwner = true;
            buttonEdit.setVisible(true);
        } else {
            buttonEdit.setVisible(false);
        }
        buttonSave.setVisible(false);
        buttonDelete.setVisible(false);
    }

    // hack
    List<Notification> nots;
    public void setMeeting(Meeting m) {
        // HACK
        nots = CalendarController.notifications;
        // ikke hack
        meeting = m;
        checkAdmin();
        setFields();
    }

    private void setFields() {
        textDesc.setEditable(editMode);
        comboOwner.setDisable(!editMode);
        comboRoom.setDisable(!editMode);
        datePicker.setDisable(!editMode);
        textFrom.setEditable(editMode);
        textTo.setEditable(editMode);
        hboxAdd.setVisible(editMode);
        hboxRemove.setVisible(editMode);

        if (meeting == null)
            return;

        addInvited(meeting.getOwner());
        comboOwner.getSelectionModel().select(meeting.getOwner());
        labelTitle.setText(meeting.getName());
        textDesc.setText(meeting.getDescription());
        datePicker.setValue(meeting.getFrom().toLocalDate());
        textFrom.setText(meeting.getFrom().format(Formatters.hhmmformat));
        textTo.setText(meeting.getTo().format(Formatters.hhmmformat));

        getParticipantsAndInvited();
    }

    /**
     * Kalles når vi skal ha data fra formen til møteobjektet.
     */
    private void editMeeting() {
        meeting.setOwner((User) comboOwner.getSelectionModel()
                .getSelectedItem());
        meeting.setName(labelTitle.getText());
        meeting.setDescription(textDesc.getText());

        LocalTime f = LocalTime.parse(textFrom.getText(),
                Formatters.hhmmformat);
        meeting.setFrom(datePicker.getValue().atStartOfDay()
                .plusMinutes(f.getHour() * 60 + f.getMinute()));

        LocalTime t = LocalTime.parse(textTo.getText(),
                Formatters.hhmmformat);
        meeting.setTo(datePicker.getValue().atStartOfDay()
                .plusMinutes(t.getHour() * 60 + f.getMinute()));
        List<Entity> participants = new LinkedList<>();
        participants.addAll(listInvited.getItems());
        participants.addAll(listParticipants.getItems());
        meeting.setParticipants(participants);
    }

    private void setOKText(Node n) {
        n.setStyle("-fx-text-fill: #333333;");
    }

    private void setErrText(Node n) {
        n.setStyle("-fx-text-fill: #ff4444;");
    }

    private boolean validateRoom() {
        return comboOwner.getSelectionModel().getSelectedItem() != null;
    }

    private boolean validateDate() {
        // Skal man kunne registrere møter i fortiden?
        if ( datePicker.getValue() == null){
            labelError.setText("Dato er ikke valgt");
            return false;
        }
        try {
            // antar at et møte bare kan vare i én dag.
            LocalDate date = datePicker.getValue();
            LocalDateTime ldt = LocalDateTime.from(date.atStartOfDay());
            meeting.setFrom(ldt);
            meeting.setTo(ldt);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean validateFromTime() {
        String string = textFrom.getText().trim();
        Matcher matcher = Regexes.Time.matcher(string);
        if (!matcher.matches()){
            labelError.setText("Fra tid er ikke gyldig");
            return false;
        }
        fromtime = LocalTime.parse(string);
        return true;
    }

    private boolean validateToTime() {
        String string = textTo.getText().trim();
        Matcher matcher = Regexes.Time.matcher(string);
        if (!matcher.matches()){
            labelError.setText("Til tid er ikke gyldig");
            return false;
        }
        totime = LocalTime.parse(string);
        return true;
    }

    private boolean validateFields() {
        // TODO: denne.
        // bør vise hvilke felter som er gale.
        boolean b =  validateRoom() && validateDate() &&
                validateFromTime() && validateToTime();
        if (!b)
            return false;
        labelError.setText("");
        int mins = totime.getHour()*60 + totime.getMinute();
        meeting.getFrom().plusMinutes(mins);

        mins = fromtime.getHour() * 60 + fromtime.getMinute();
        meeting.getTo().plusMinutes(mins);

        return true;
    }

    public void clickEdit(ActionEvent actionEvent) {
        if (editMode){
            // Vi er ferdig å redigere
            setMeeting(meeting);
            editMode = false;
            buttonEdit.setText("Rediger");
            buttonSave.setVisible(false);
            buttonDelete.setVisible(false);
            buttonBack.setDisable(false);
        } else {
            // Vi skal redigere
            editMode = true;
            buttonEdit.setText("Avbryt");
            buttonSave.setVisible(true);
            buttonDelete.setVisible(true);
            buttonBack.setDisable(true);

            getRooms();
            getUsers();
        }
        setFields();
    }

    public void clickBack(ActionEvent actionEvent) {
        // hack
        CalendarController.notifications = nots;
        // Må sende PUT request til server, for å oppdatere møtet.
        Platform.runLater(() -> getApplication().setScene(getStage(), Layout.Calendar));
    }

    public void clickDelete(ActionEvent actionEvent) {
        // verifiser
        Request req = new MeetingRequest(Request.Type.DELETE, meeting);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL)
                    return;
                Platform.runLater(() -> getApplication().setScene(getStage(), Layout.Calendar));
                getClient().removeListener(this);
            }
        });
    }

    public void clickSave(ActionEvent actionEvent) {
        if (!validateFields())
            return;

        editMeeting();
        Request req = new MeetingRequest(Request.Type.PUT, meeting);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL)
                    return;

                editMode = false;
                Platform.runLater(() -> {
                    buttonEdit.setText("Rediger");
                    buttonSave.setVisible(false);
                    buttonDelete.setVisible(false);
                    buttonBack.setDisable(false);
                });
            }
        });
    }

    private void addInvited(User u) {
        comboInvite.getItems().remove(u);
        comboRemove.getItems().add(u);
        listInvited.getItems().add(u);
    }

    private void addConfirmed(User u) {
        listParticipants.getItems().add(u);
        listInvited.getItems().remove(u);
    }

    private void removeInvitedOrConfirmed(User u) {
        comboInvite.getItems().add(u);
        comboRemove.getItems().remove(u);

        listInvited.getItems().remove(u);
        listParticipants.getItems().remove(u);
    }

    public void inviteUser(Entity e) {
        if (e == null) return;
        User u = (User) e;
        comboInvite.getSelectionModel().clearSelection();
        addInvited(u);
    }

    public void removeUser(Entity e) {
        if (e == null) return;
        User u = (User) e;
        comboRemove.getSelectionModel().clearSelection();
        removeInvitedOrConfirmed(u);
    }
}
