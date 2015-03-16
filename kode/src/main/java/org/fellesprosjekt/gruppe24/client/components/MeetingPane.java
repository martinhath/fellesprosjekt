package org.fellesprosjekt.gruppe24.client.components;

import javafx.scene.layout.Pane;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

public class MeetingPane extends Pane {

    private final String[] Colors = {
            "33b5e5", "aa66cc", "99cc00", "ffbb33", "ff4444"
    };

    private Meeting meeting;

    private String getColor() {
        return Colors[meeting.hashCode() % Colors.length];
    }

    public MeetingPane(Meeting meeting) {
        this.meeting = meeting;

        setStyle(getStyle() + "-fx-border-color: " + getColor());
    }

}
