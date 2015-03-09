package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CalendarController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML private Button meetingButton;
    @FXML private Label labelMon;
    @FXML private Label labelTue;
    @FXML private Label labelWed;
    @FXML private Label labelThu;
    @FXML private Label labelFri;
    @FXML private Label labelSat;
    @FXML private Label labelSun;
    @FXML private Label labelWeek;
    @FXML private Label labelMonth;

    private DateTimeFormatter dayformat = DateTimeFormatter.ofPattern("EEEE d.");
    private DateTimeFormatter weekformat = DateTimeFormatter.ofPattern("w");
    private DateTimeFormatter monthformat = DateTimeFormatter.ofPattern("MMMM");

    private LocalDateTime date;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        date = LocalDateTime.now();
        init();
    }

    private void init() {
        int i = date.getDayOfWeek().getValue();
        setCalendarLabels(date.minusDays(i-1));
    }

    /**
     * Setter alle lablene til uken som begynner med `date` datoen.
     * @param date Datoen som uken begynner på.
     */
    private void setCalendarLabels(LocalDateTime date) {
        labelMon.setText(date.plusDays(0).format(dayformat));
        labelTue.setText(date.plusDays(1).format(dayformat));
        labelWed.setText(date.plusDays(2).format(dayformat));
        labelThu.setText(date.plusDays(3).format(dayformat));
        labelFri.setText(date.plusDays(4).format(dayformat));
        labelSat.setText(date.plusDays(5).format(dayformat));
        labelSun.setText(date.plusDays(6).format(dayformat));

        labelWeek.setText("Uke " + date.format(weekformat));
        labelMonth.setText(date.format(monthformat));
    }

    @FXML
    public void clickPrevWeek(ActionEvent e) {
        date = date.minusWeeks(1);
        setCalendarLabels(date);
    }

    @FXML
    public void clickNextWeek(ActionEvent e) {
        date = date.plusWeeks(1);
        setCalendarLabels(date);
    }

    @FXML
    public void newMeeting(ActionEvent a) {
        String path = Layout.NewMeeting;
        getApplication().newScene(path);
    }

    @FXML
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
