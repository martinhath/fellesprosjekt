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
        System.out.println("PUT User");
    }

    public void get(Request req){
        System.out.println("GET User");
    }

    public void auth(Request req){
        System.out.println("AUTH User");

        Response res = new Response();
        if (connection.getUser() != null){
            res.setType(Response.Type.FAILURE);
            res.setModel(String.class);
            res.setPayload("You are already logged in");
            connection.sendTCP(res);
            return;
        }
        LoginInfo loginInfo = (LoginInfo) req.getPayload();
        User user = login(loginInfo);
        if (user == null) {
            res.setType(Response.Type.FAILURE);
            connection.sendTCP(res);
            return;
        }
        res.setType(Response.Type.SUCCESS);
        res.setPayload(user);
        connection.sendTCP(res);
        connection.setUser(user);
    }

    private User login(LoginInfo loginInfo){
        /**
         * Håndter login-logikk
         * Returnerer om login er good.
         */
        return new User();
    }

}
