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
        LoginInfo login = (LoginInfo) req.getPayload();

        User user = new User();
        user.setUsername(login.getUsername());
        user.setPassword(login.getPassword());

        connection.setUser(user);
    }

    public void get(Request req){
        System.out.println("GET " + req.getModel());
    }

    public void auth(Request req){
        System.out.println("GET " + req.getModel());

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
        res.setType(user != null ? Response.Type.SUCCESS : Response.Type.FAILURE);
        res.setPayload(user);
        connection.sendTCP(res);
    }

    private User login(LoginInfo loginInfo){
        /**
         * Håndter login-logikk
         * Returnerer om login er good.
         */
        return new User();
    }

}
