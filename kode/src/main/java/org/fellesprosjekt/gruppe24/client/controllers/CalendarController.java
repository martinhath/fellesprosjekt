package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.components.MeetingPane;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.models.GroupNotification;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.MeetingNotification;
import org.fellesprosjekt.gruppe24.common.models.Notification;
import org.fellesprosjekt.gruppe24.common.models.net.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.*;
import java.util.logging.Logger;

public class CalendarController extends ClientController {

    private Logger logger = Logger.getLogger(getClass().getName());

    @FXML private Label labelMon;
    @FXML private Label labelTue;
    @FXML private Label labelWed;
    @FXML private Label labelThu;
    @FXML private Label labelFri;
    @FXML private Label labelSat;
    @FXML private Label labelSun;
    @FXML private Label labelWeek;
    @FXML private Label labelMonth;
    @FXML private Text textBruker;

    @FXML private Button buttonNotification;
    
    @FXML private GridPane calendarGrid;
    @FXML private ScrollPane scrollPane;

    private LocalDateTime date;

    // top kek
    public static List<Meeting> meetings = new LinkedList<>();
    public static List<Notification> notifications = new LinkedList<>();

    private static boolean isInited = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        date = LocalDateTime.now();
        setCalendarLabels(date);
        
        setDefaultScrollPosition();

        showNotificationCount();
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) return;
                if (!listInstanceOf(res.payload, Meeting.class)) return;

                meetings = (List<Meeting>) res.payload;
                Platform.runLater(CalendarController.this::showMeetings);
            }
        });
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) return;
                if (!listInstanceOf(res.payload, MeetingNotification.class) &&
                        !listInstanceOf(res.payload, GroupNotification.class)) {
                    return;
                }
                notifications = (List<Notification>) res.payload;
                Platform.runLater(CalendarController.this::showNotificationCount);
            }
        });
    }

    @Override
    public void focus() {
        showNotificationCount();
        showMeetings();
    }
    
    private void setDefaultScrollPosition() {
    	// 08:00 er øverst
        double kl8 = (scrollPane.getVmax() - scrollPane.getVmin()) * 10 / 13;
        scrollPane.setVvalue(kl8);
    }

    @Override
    public void init() {
        textBruker.setText("Logget inn som " + getApplication().getUser().getUsername());

        // Får tak i alle møter
        if (!isInited) {
            Request req = new MeetingRequest(Request.Type.LIST, getApplication().getUser());
            getClient().sendTCP(req);
            req = new NotificationRequest(Request.Type.LIST,
                    false, NotificationRequest.Handler.BOTH, getApplication().getUser());
            getClient().sendTCP(req);

            isInited = true;
        } else {
            showMeetings();
            showNotificationCount();
        }
    }

    private void showNotificationCount() {
        if (notifications == null) {
            buttonNotification.setText("Ingen nye meldinger");
            return;
        }
        int size = (int) notifications.stream()
                .filter((n) -> !n.isRead())
                .count();
        if (size == 0)
            buttonNotification.setText("Ingen nye meldinger");
        else
            buttonNotification.setText(String.format("Nye meldinger (%s)", size));
    }

    /**
     * Sørger for at alle møter for denne uken vises i kalenderen.
     */
    private void showMeetings() {
        // Fjerner møtene som vises nå.
        List<Node> toRemove = new LinkedList<>();
        calendarGrid.getChildren().forEach((node) -> {
            if (node instanceof Pane)
                toRemove.add(node);
        });
        for (Node node : toRemove)
            calendarGrid.getChildren().remove(node);

        markToday();
        
        // Sjekker om møtet kan bli gjemt bak andre møter i visningen og sier da ifra om det
        for (int i = 0; i < meetings.size(); i++) {
            boolean hidden = false;
            if (i > 0 && i < meetings.size() - 1) {
                hidden =
                        meetings.get(i - 1).getTo().isAfter(meetings.get(i).getTo()) ||
                        meetings.get(i + 1).getFrom().isBefore(meetings.get(i).getTo());
            }
            showMeeting(meetings.get(i), hidden);
        }
        setDefaultScrollPosition();
    }

    private void markToday() {
		LocalDateTime now = LocalDateTime.now();
		WeekFields weekFields = WeekFields.of(Locale.getDefault()); 
		int weekNumber = now.get(weekFields.weekOfWeekBasedYear());	
		
		if (weekNumber == date.get(weekFields.weekOfWeekBasedYear())) {
			int col = now.getDayOfWeek().getValue();
			int row = now.getHour();
			Pane day = new Pane();
			Pane time = new Pane();

			Node n = calendarGrid.getChildren().get(row);
            calendarGrid.add(day, col, 0);
			calendarGrid.add(time, 0, row);
            day.toBack();
            time.toBack();

			GridPane.setRowSpan(day, 24);
			GridPane.setColumnSpan(time, 8);
			day.setStyle("-fx-background-color: rgba(235, 251, 255, 1)");
			time.setStyle("-fx-background-color: rgba(235, 251, 255, 1)");
			
		}					
	}

    /**
     * Tegner et møte i kalenderen dersom det er i uken som vises
     * @param m møtet som skal tegnes
     * @param hidden om det kan skules fullstendig bak et annet møte
     */
    private void showMeeting(Meeting m, boolean hidden) {
        LocalDateTime from = m.getFrom();
        LocalDateTime to = m.getTo();

        // Sjekker om møtet begynner i neste uke, eller sluttet i forige uke.
        LocalDateTime mon = date.minusDays(date.getDayOfWeek().getValue() - 1).withHour(0);
        LocalDateTime sun = date.plusDays(7 - date.getDayOfWeek().getValue()).withHour(23);
        if (from.isAfter(sun)) return;
        if (to.isBefore(mon)) return;

        int col = from.getDayOfWeek().getValue();
        int row = from.getHour();


        MeetingPane pane = new MeetingPane(this, m);
        if (hidden) {
            // TODO gjøre den litt breiere sånn at den er synlig uansett?
        }
        calendarGrid.add(pane, col, row);
        int duration = m.getTo().getHour() - m.getFrom().getHour() + 1;
        GridPane.setRowSpan(pane, duration);
    }


    /**
     * Setter alle lablene til uken som begynner med `date` datoen.
     *
     * @param date Datoen som uken begynner på.
     */
    private void setCalendarLabels(LocalDateTime date) {
        int i = date.getDayOfWeek().getValue();
        date = date.minusDays(i - 1);
        labelMon.setText(date.plusDays(0).format(Formatters.dayformat));
        labelTue.setText(date.plusDays(1).format(Formatters.dayformat));
        labelWed.setText(date.plusDays(2).format(Formatters.dayformat));
        labelThu.setText(date.plusDays(3).format(Formatters.dayformat));
        labelFri.setText(date.plusDays(4).format(Formatters.dayformat));
        labelSat.setText(date.plusDays(5).format(Formatters.dayformat));
        labelSun.setText(date.plusDays(6).format(Formatters.dayformat));

        labelWeek.setText("Uke " + date.format(Formatters.weekformat));
        labelMonth.setText(date.format(Formatters.monthformat));
    }

    @FXML
    public void clickPrevWeek(ActionEvent e) {
        date = date.minusWeeks(1);
        setCalendarLabels(date);
        showMeetings();
    }

    @FXML
    public void clickNextWeek(ActionEvent e) {
        date = date.plusWeeks(1);
        setCalendarLabels(date);
        showMeetings();
    }

    @FXML
    public void newMeeting(ActionEvent a) {
        String path = Layout.NewMeeting;
        getApplication().newScene(path);
    }

    @FXML
    public void clickCurrentWeek(ActionEvent e) {
        date = LocalDateTime.now();
        setCalendarLabels(date);
        showMeetings();
    }

    @FXML
    public void seeNotifications(ActionEvent a) {
        String path = Layout.Notification;
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
                if (res.type == Response.Type.OK) {
                    Platform.runLater(() ->
                                    getApplication().setScene(getStage(), Layout.Login)
                    );
                } else {
                    logger.warning((String) res.payload);
                }
                getClient().removeListener(this);
            }
        });
    }
}
