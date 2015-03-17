package org.fellesprosjekt.gruppe24.client.controllers;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by ingridvold on 17.03.15.
 */
public class NewuserController extends ClientController{


    //brukerinformasjon
    @FXML
    private TextField userName;
    @FXML
    private TextField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;

    //knapper
    @FXML
    private Button ok;
    @FXML
    private Button quit;


    private boolean emptyFieldUser() {
        String user = userName.getText();
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFName() {
        String fName = firstName.getText();
        if (fName == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateLName() {
        String lName = lastName.getText();
        if (lName == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordText = password.getText();
        if (passwordText == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        String mail = email.getText();
        if (mail == null) {
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFields() {
        boolean b = emptyFieldUser() && validateFName() &&
                validateLName() && validatePassword() &&
                validateEmail();
        if (!b)
            return false;
    }
    else {
        return true;
    }

    public void clickOk(ActionEvent e){

    }

}
