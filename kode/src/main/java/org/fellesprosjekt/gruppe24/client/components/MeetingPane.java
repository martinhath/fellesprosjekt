package org.fellesprosjekt.gruppe24.client.components;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.client.Layout;
import org.fellesprosjekt.gruppe24.client.controllers.ClientController;
import org.fellesprosjekt.gruppe24.client.controllers.MeetingDetailController;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

public class MeetingPane extends VBox {

    private final String[] Colors = {
            "33b5e5", "aa66cc", "669900", "ffbb33", "ff4444"
    };

    private Meeting meeting;
    private ClientController controller;

    private String getColor() {
        return Colors[Math.abs(meeting.getName().hashCode()) % Colors.length];
    }

    public MeetingPane(ClientController controller, Meeting meeting) {
        this.controller = controller;
        this.meeting = meeting;

        // Styling mm. til selve boksen
        getStyleClass().add("meeting-box");
        String s_0 = getStyle();
        String style = String.format(
                        "-fx-background-color: #%s;",
                getColor());
        setStyle(s_0 + style);
        hoverProperty().addListener((observableValue, aBoolean, t1) -> {
            String style1 = String.format(
                    "-fx-background-color: derive(#%s, %d%s);",
                    getColor(), isHover() ? +20 : 0, "%");
            setStyle(s_0 + style1);
            toFront();
        });

        setOnMouseClicked((MouseEvent e) -> {
            MeetingDetailController c = (MeetingDetailController)
                    controller.getApplication().setScene(controller.getStage(),
                    Layout.MeetingDetail);
            c.setMeeting(meeting);
        });


        // Legger inn ting som skal vises
        Label title = new Label();
        title.getStyleClass().add("meeting-box-title");
        title.setText(meeting.getName());
        getChildren().add(title);

        Label time = new Label();
        time.getStyleClass().add("meeting-box-time");
        String text = meeting.getFrom().format(Formatters.hhmmformat) +
                " - " + meeting.getTo().format(Formatters.hhmmformat);
        time.setText(text);
        getChildren().add(time);

        Label descLabel = new Label();
        descLabel.getStyleClass().add("meeting-box-desc");
        String desc = meeting.getDescription();
        descLabel.setText(desc);
        descLabel.setWrapText(true);
        getChildren().add(descLabel);
    }

}
