package org.fellesprosjekt.gruppe24.client.components;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

public class MeetingPane extends VBox {

    private final String[] Colors = {
            "51, 181, 229, .5", "170, 102, 204, .5",
            "153, 204, 0, .5", "255, 187, 51, .5", "255, 68, 68, .5"
    };

    private Meeting meeting;

    private String getColor() {
        return Colors[meeting.hashCode() % Colors.length];
    }

    public MeetingPane(Meeting meeting) {
        getStyleClass().add("meeting-box");

        this.meeting = meeting;

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

        String style = String.format("%s" +
                "-fx-border-color: rgba(%s);",
                getStyle(), getColor());
        setStyle(style);
    }

}
