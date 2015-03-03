package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Notification;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
    
   
    
    
    public void setNotification(Notification notification) {
    	Meeting meeting = notification.getMeeting();
    	descriptionField.setText(meeting.getDescription());
    	roomField.setText(meeting.getRoom().toString());
    	startField.setText(meeting.getFrom().toString());
    	endField.setText(meeting.getTo().toString());
    	leaderField.setText(meeting.getOwner().toString());
    	participantField.getItems().addAll(meeting.getParticipants());
    	
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
    
    
    
    
    
    
	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
	

    }
}
