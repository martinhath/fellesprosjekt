package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Notification;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class InvitationController extends ClientController {
	
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField roomField;
    @FXML
    private TextField startField;
    @FXML
    private TextField endField;
    @FXML
    private TextField leaderField;
    @FXML
    private ListView<Entity> participantField;
    
    //radiobuttons
    @FXML
    private RadioButton aksepter;
    @FXML
    private RadioButton avvis;
    @FXML
    private RadioButton venter;
    //grupp för buttons
    @FXML
    private ToggleGroup group;
    
    public void setNotification(Notification notification) {
    	Meeting meeting = notification.getMeeting();
    	descriptionField.setText(meeting.getDescription());
    	roomField.setText(meeting.getRoom().toString());
    	startField.setText(meeting.getFrom().toString());
    	endField.setText(meeting.getTo().toString());
    	leaderField.setText(meeting.getOwner().toString());
    	participantField.getItems().addAll(meeting.getParticipants());
    	
    }
    
    //se vilken radiobutton som är vald
    public void selectedButton() {
    	this.aksepter.setSelected(true);
    	final Label changed = new Label("Välj en knapp");
    	group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
    		public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
    			changed.setText("Vald knapp har ändrats");
    		}
    	});
  
    }
    
    

    
    
    //sänd till server vilket val användaren tog 
    public void pressOkButton(ActionEvent e) {
    	
    }
    
    
    //avbryt ocg gå tillbaka till kalenderview
    public void pressAvbrytButton(ActionEvent e) {
        getApplication().setScene("/layout/Kalender.fxml");
    }
    
    //skjul invitationen från notification?
    public void pressSkjulButton(ActionEvent e) {
    	
    }
    
    
    
    

}
