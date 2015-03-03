package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController extends ServerController{
    Logger logger = Logger.getLogger(getClass().getName());

    public UserController(ServerConnection conn) {
        super(conn);
    }

    public void put(Request req){
        System.out.println("PUT User");
    }

    public void get(Request req){
        System.out.println("GET User");
    }

    public void post(Request req){
        logger.log(Level.INFO, "POST User");

        Response res = new Response();
        if (connection.getUser() != null){
            res = Response.GetFailResponse("You are already logged in");
            connection.sendTCP(res);
            return;
        }
        LoginInfo loginInfo = (LoginInfo) req.payload;
        User user = login(loginInfo);
        if (user == null) {
            res = Response.GetFailResponse("Username or password was wrong.");
            connection.sendTCP(res);
            return;
        }
        res.type = Response.Type.OK;
        res.payload = user;
        connection.sendTCP(res);

        connection.setUser(user);
    }

    private User login(LoginInfo loginInfo){
        /**
         * Håndter login-logikk
         * Returnerer om login er good.
         */
        User user = UserDatabaseHandler.authenticate(
                loginInfo.getUsername(),
                loginInfo.getPassword());
        Logger.getLogger(getClass().getName()).log(Level.INFO, "User: " + user);
        return user;
    }

}
