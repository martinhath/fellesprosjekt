package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.List;
import java.util.logging.Logger;

public class UserController extends ServerController{
    Logger logger = Logger.getLogger(getClass().getName());

    public UserController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public void put(Request req){
        if (!(req.payload instanceof User)) {
            connection.sendTCP(new Response(Response.Type.FAIL,
                    "Wrong payload: not User"));
            return;
        }
        Response res = new Response();
        User user = (User) req.payload;
        UserDatabaseHandler.updateUser(user);
        user = UserDatabaseHandler.getUserFromUsername(user.getUsername());
        if (user == null) {
            res.type = Response.Type.FAIL;
            res.payload = "Something went wrong.";
        } else {
            res.type = Response.Type.OK;
            res.payload = user;
        }
        connection.sendTCP(res);
    }

    @Override
    public void get(Request req){
        Response res = new Response();
        if (req.payload instanceof Integer) {
            // payload er Id
            Integer id = (Integer) req.payload;
            res.payload = UserDatabaseHandler.getById(id);
            res.type = Response.Type.OK;
        } else if (req.payload instanceof String) {
            // payload er brukernavn
            String username = (String) req.payload;
            res.payload = UserDatabaseHandler.getUserFromUsername(username);
            res.type = Response.Type.OK;
        } else {
            res.type = Response.Type.FAIL;
            res.payload = "Unknown payload type.";
        }
        connection.sendTCP(res);
    }

    @Override
    public void list(Request req) {
        Response res = new Response();
        res.type = Response.Type.OK;
        List<User> users = UserDatabaseHandler.getAllUsers();
        res.payload = users;
        connection.sendTCP(res);
        logger.info("sendt");
    }

    @Override
    public void post(Request req) {
        if (!(req.payload instanceof User)) {
            connection.sendTCP(new Response(Response.Type.FAIL,
                    "Wrong payload: not User"));
            return;
        }
        Response res = new Response();
        User user = (User) req.payload;
        int ret = UserDatabaseHandler.addNewUser(user, user.getPassword());
        if (ret == -1) {
            res.type = Response.Type.FAIL;
            res.payload = "Something went wrong.";
        } else {
            res.type = Response.Type.OK;
        }
        connection.sendTCP(res);
    }
}
