package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthController extends ServerController{

    private Logger logger = Logger.getLogger(getClass().getName());

    public AuthController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public void post(Request req) {
        logger.log(Level.INFO, "POST User");
        LoginInfo loginInfo = (LoginInfo) req.payload;

        if (loginInfo == null) {
            logout();
        } else if (connection.getUser() != null) {
            connection.sendTCP(
                    Response.GetFailResponse("You are already logged in"));
        } else {
            login(loginInfo);
        }
    }

    @Override
    public void put(Request req) {
        throw new NotImplementedException();
    }

    @Override
    public void get(Request req) {
        throw new NotImplementedException();
    }

    @Override
    public void list(Request req) {
        throw new NotImplementedException();
    }

    private boolean login(LoginInfo loginInfo){
        Logger.getLogger(getClass().getName()).log(
                Level.INFO, "User login: " + loginInfo);
        Response res = new Response();
        User user = UserDatabaseHandler.authenticate(
                loginInfo.getUsername(), loginInfo.getPassword());
        if (user == null) {
            Logger.getLogger(getClass().getName()).log(
                    Level.INFO, "Failed to log in.");
            res = Response.GetFailResponse("Username or password was wrong.");
            connection.sendTCP(res);
            return false;
        }
        connection.setUser(user);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "User: " + user);

        res.type = Response.Type.OK;
        res.payload = user;
        connection.sendTCP(res);
        Logger.getLogger(getClass().getName()).log(
                Level.INFO, "Login success.");
        return true;
    }

    private boolean logout() {
        if (connection.getUser() == null ) {
            connection.sendTCP(
                    Response.GetFailResponse("You are already logged in"));
            return false;
        }
        User user = connection.getUser();

        // Trenger vi å ha med user payloaden?
        Response res = new Response(Response.Type.OK, user);
        connection.setUser(null);
        connection.sendTCP(res);
        return true;
    }
}
