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
        System.out.println("Fikk en bruker:");
        System.out.println("Brukernavn: " + login.getUsername());
        System.out.println("Passord:    " + login.getPassword());

        User user = new User();
        user.setUsername(login.getUsername());
        user.setPassword(login.getPassword());

        connection.setUser(user);
    }

    private boolean login(LoginInfo loginInfo){
        /**
         * Håndter login-logikk
         * Returnerer om login er good.
         */
        return true;
    }

    public void get(Request req){
        /**
         * Kanskje det finnes en bedre måte å håndtere alt dette?
         */
        System.out.println("Request: "+req);
        Response res = new Response();
        User user = connection.getUser();
        if (user == null){
            res.setType(Response.Type.FAILURE);
            if (req.getPayload() instanceof LoginInfo){
                LoginInfo loginInfo = (LoginInfo) req.getPayload();
                boolean success = login(loginInfo);
                if (success){
                    res.setType(Response.Type.SUCCESS);
                }
                else{
                    res.setType(Response.Type.FAILURE);
                }
            }
        } else {
            res.setType(Response.Type.SUCCESS);
            res.setPayload(user);
            res.setModel(User.class);
        }
        connection.sendTCP(res);
    }

}
