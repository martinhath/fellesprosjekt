package org.fellesprosjekt.gruppe24.client.components;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.fellesprosjekt.gruppe24.client.Formatters;
import org.fellesprosjekt.gruppe24.common.models.Meeting;

public class MeetingPane extends VBox {

    private final String[] Colors = {
            "33b5e5", "aa66cc", "669900", "ffbb33", "ff4444"
    };

    private Meeting meeting;

    private String getColor() {
        return Colors[meeting.hashCode() % Colors.length];
    }

    public MeetingPane(Meeting meeting) {
        this.meeting = meeting;

        // Styling mm. til selve boksen
        getStyleClass().add("meeting-box");
        for (String s : getStyleClass()){
            System.out.println(s);
        }
        String s_0 = getStyle();
        String style = String.format(
                        "-fx-background-color: #%s;",
                getColor());
        setStyle(s_0 + style);

        hoverProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                String style = String.format(
                        "-fx-background-color: derive(#%s, %d%s);",
                        getColor(), isHover() ? +20 : 0, "%");
                setStyle(s_0 + style);
            }
        });

        setOnMouseClicked((MouseEvent e) -> {
            System.out.println("halla");
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
