package org.fellesprosjekt.gruppe24.client.Components;

import javafx.scene.layout.Pane;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

public class MeetingPane extends Pane {

    private Meeting meeting;

    public MeetingPane(Meeting meeting) {
        this.meeting = meeting;
    }

}
