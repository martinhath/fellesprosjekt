package org.fellesprosjekt.gruppe24.client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.InvitationResponse;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Notification;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

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
	
	private Meeting meeting;
	
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
    	meeting = notification.getMeeting();
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
    			RadioButton ch = (RadioButton)new_toggle.getToggleGroup().getSelectedToggle();
    		}
    	});
  
    }
    
    public boolean buttonIsNotEmpty() {
    	if (group == null) {
    		System.out.println("ingen knapp har blivit vald");
    		return false;
    	} else {
    		return true;
    	}
    }
    
    

    
    
    //sänd till server vilket val användaren tog 
    public void pressOkButton(ActionEvent e) {
    	
    	
    	
        Request req = new Request(Request.Type.PUT, Meeting.class);
        InvitationResponse inv = new InvitationResponse(meeting.getId(), InvitationResponse.Answer.YES);
        req.setPayload(inv);
        
        Client client = getClient();
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection conn, Object obj) {
                if (!(obj instanceof Response)) {
                    System.err.println("Response error: " + obj);
                    return;
                }
                Response res = (Response) obj;

                if (res.getType() == Response.Type.SUCCESS) {
                    handleOK();
                } else {
                    handleRejectedOK();
                }
                client.removeListener(this);
            }
        });
    }
    
    public void handleOK(){
        System.out.println("Svaret har sparats");
        getApplication().setScene("/layout/Kalender.fxml");
    }
    
    public void handleRejectedOK(){
        System.out.println("Välj en knapp");
    }
    
    
    //avbryt och gå tillbaka till kalenderview
    public void pressAvbrytButton(ActionEvent e) {
        getApplication().setScene("/layout/Kalender.fxml");
    }
    
    //skjul invitationen från notification?
    public void pressSkjulButton(ActionEvent e) {
    	
    }
    
    
    
    

}
