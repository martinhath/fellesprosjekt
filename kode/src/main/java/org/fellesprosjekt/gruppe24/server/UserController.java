package org.fellesprosjekt.gruppe24.server;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

public class UserController extends ServerController{

    public UserController(ServerConnection conn) {
        super(conn);
    }

    public void put(Request req){
        System.out.println("UserController.put()");
        LoginInfo login = (LoginInfo) req.getPayload();
        System.out.println("Fikk en bruker:");
        System.out.println("Brukernavn: " + login.getUsername());
        System.out.println("Passord:    " + login.getPassword());

        User user = new User("");
        user.setUsername(login.getUsername());
        user.setPassword(login.getPassword());

        connection.setUser(user);
    }

    public void get(Request req){
        System.out.println("UserController.get()");
        /**
         * Sender tilbake brukeren som er logget på.
         * Bør sjekke req om man har gitt med logininfo,
         * da skal den sende tilbake en bruker dersom infoen
         * finnes i databasen.
         */
        Response res = new Response();
        User user = connection.getUser();
        if (user == null){
            res.setType(Response.Type.FAILURE);
        } else {
            res.setType(Response.Type.SUCCESS);
            res.setPayload(user);
        }
        connection.sendTCP(res);
    }

}
