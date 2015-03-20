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
    public Response put(Request req){
        if (!(req.payload instanceof User)) {
            return Response.GetFailResponse(
                    "Wrong payload: not User");
        }
        UserDatabaseHandler handler = UserDatabaseHandler.GetInstance();
        Response res = new Response();
        User user = (User) req.payload;
        handler.insert(user);
        user = handler.getUserFromUsername(user.getUsername());
        if (user == null) {
            res.type = Response.Type.FAIL;
            res.payload = "Something went wrong.";
        } else {
            res.type = Response.Type.OK;
            res.payload = user;
        }
        return res;
    }

    @Override
    public Response get(Request req){
        UserDatabaseHandler handler = UserDatabaseHandler.GetInstance();
        Response res = new Response();
        if (req.payload instanceof Integer) {
            // payload er Id
            Integer id = (Integer) req.payload;
            res.payload = handler.get(id);
            res.type = Response.Type.OK;
        } else if (req.payload instanceof String) {
            // payload er brukernavn
            String username = (String) req.payload;
            res.payload = handler.getUserFromUsername(username);
            res.type = Response.Type.OK;
        } else {
            res.type = Response.Type.FAIL;
            res.payload = "Unknown payload type.";
        }
        return res;
    }

    @Override
    public Response list(Request req) {
        UserDatabaseHandler handler = UserDatabaseHandler.GetInstance();
        Response res = new Response();
        res.type = Response.Type.OK;
        List<User> users = handler.getAll();
        if (users == null){
            return Response.GetFailResponse("Database returned null.");
        }
        res.payload = users;
        return res;
    }

    @Override
    public Response post(Request req) {
        if (!(req.payload instanceof User)) {
            return Response.GetFailResponse(
                    "Wrong payload: not User");
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
        return res;
    }

	@Override
	public Response delete(Request req) {
		if (!(req.payload instanceof User)) {
            return Response.GetFailResponse(
                    "Wrong payload: not User");
        }
		User user = (User) req.payload;
		Response res = new Response();
		UserDatabaseHandler handler = UserDatabaseHandler.GetInstance();
		if(handler.delete(user)) {
			res.type = Response.Type.OK;
		} else {
			res.type = Response.Type.FAIL;
			res.payload = "Something went wrong.";
		}
        return res;
	}
}
