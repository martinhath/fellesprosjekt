package org.fellesprosjekt.gruppe24.client.controllers;

import com.esotericsoftware.kryonet.Connection;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;

import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.Regexes;
import org.fellesprosjekt.gruppe24.common.models.*;
import org.fellesprosjekt.gruppe24.common.models.net.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class GroupController extends ClientController {
    private Logger logger = Logger.getLogger(getClass().getName());

    private Group group;

    //FXML-fält
    @FXML private TextField fieldName;
    @FXML private CheckComboBox<User> dropdownMembers;
    @FXML private Button buttonOk;
    @FXML private Button buttonAbort;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        getAllUsers();
    }

    private void getAllUsers() {
        UserRequest req = new UserRequest(Request.Type.LIST, null);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener(){
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL) {
                    getClient().removeListener(this);
                    logger.info((String) res.payload);
                    return;
                }

                if (!listInstanceOf(res.payload, User.class)) return;

                List<User> list = (List<User>) res.payload;
                dropdownMembers.getItems().addAll(list);
                setCurrentSelected();
                getClient().removeListener(this);
            }
        });
    }

    private void setCurrentSelected() {
        for(User e : dropdownMembers.getItems()) {
                if(getApplication().getUser().equals(e)) {
                    Platform.runLater(() -> {
                        dropdownMembers.checkModelProperty().getValue().check(e);
                    });
            }
        }
    }

    private void setOKText(Node n) {
        n.setStyle("-fx-text-fill: #333333;");
    }

    private void setErrText(Node n) {
        n.setStyle("-fx-text-fill: #ff4444;");
    }

    public void init() {
        group = new Group();
    }


    /**
     * == VALIDERING ==
     * Dersom et felt ikke er obligatorisk (er det noen)
     * så må validate returnere `true` hvis det er tomt.
     */

    private boolean validateGroupName() {
    	String string = fieldName.getText().trim();
    	Matcher matcher = Regexes.Text.matcher(string);
    	if (!matcher.matches()) {
    		//vis felmeddelande
            System.out.println("name fails");
            return false;
    	}
    	return true;
    }

    private boolean validateMembers() {
        if (dropdownMembers.getCheckModel().getCheckedItems().size() == 0){
            return false;
        }
        return true;
    }

    private boolean validateFields() {
        boolean b =  validateGroupName() && validateMembers();
        if (!b) return false;

        group.setName(fieldName.getText());
        group.setOwner(getApplication().getUser());

        List<User> members = new LinkedList<>();
        for (User e : dropdownMembers.getCheckModel().getCheckedItems()){
            System.out.println(e);
            group.addMember(e);
            System.out.println(group.getMembers().size());
        }
        return true;
    }

    public void clickOk(ActionEvent actionEvent) {
        if (!validateFields())
            return;

        System.out.println(group);
        Request req = new GroupRequest(Request.Type.POST, group);
        getClient().sendTCP(req);
        getClient().addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection conn, Response res) {
                if (res.type == Response.Type.FAIL){
                    logger.info((String) res.payload);
                    // vis noe på skjermen om at det skjedde en feil
                    return;
                }
                Platform.runLater(() -> {
                    getApplication().removeStage(getStage());
                });
                getClient().removeListener(this);
            }
        });
    }

    public void clickAbort(ActionEvent actionEvent) {
        // TODO: Kanskje legge inn en 'er du sikker?' hvis vi har noe data
        getApplication().removeStage(getStage());
    }
}
