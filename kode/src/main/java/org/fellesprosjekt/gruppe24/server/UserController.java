package org.fellesprosjekt.gruppe24.server;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.Request;

public class UserController {

    public void handleRequest(Request req){
        LoginInfo login = (LoginInfo) req.getPayload();
        System.out.println("Fikk en bruker:");
        System.out.println("Brukernavn: " + login.getUsername());
        System.out.println("Passord:    " + login.getPassword());
    }
}
