package org.fellesprosjekt.gruppe24.server.controllers;

import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.Notification;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.NotificationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.fellesprosjekt.gruppe24.server.ServerConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthController extends ServerController{

    private Logger logger = Logger.getLogger(getClass().getName());

    public AuthController(ServerConnection conn) {
        super(conn);
    }

    @Override
    public Response post(Request r) {
        AuthRequest req = (AuthRequest) r;
        logger.log(Level.INFO, "POST User");
        LoginInfo loginInfo = (LoginInfo) req.payload;

        if (req.action == AuthRequest.Action.LOGIN) {
            return login(loginInfo);
        } else if (req.action == AuthRequest.Action.LOGOUT) {
            return logout();
        }
        return null;
    }

    @Override
    public Response put(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public Response get(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public Response list(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    private Response login(LoginInfo loginInfo){
        Logger.getLogger(getClass().getName()).log(
                Level.INFO, "User login: " + loginInfo);
        if (connection.getUser() != null) {
            return Response.GetFailResponse("You are already logged in");
        }
        Response res = new Response();
        User user = UserDatabaseHandler.GetInstance().authenticate(
                loginInfo.getUsername(), loginInfo.getPassword());
        if (user == null) {
            Logger.getLogger(getClass().getName()).log(
                    Level.INFO, "Failed to log in.");
            return Response.GetFailResponse("Username or password was wrong.");
        }
        connection.setUser(user);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "User: " + user);

        res.type = Response.Type.OK;
        res.payload = user;

        logger.info("User " + connection.getUser() + " logged in.");

        return res;
    }

    private Response logout() {
        if (connection.getUser() == null ) {
            return Response.GetFailResponse("You are not logged in");
        }
        User user = connection.getUser();

        // Trenger vi å ha med user payloaden?
        Response res = new Response(Response.Type.OK, user);
        logger.info("User " + connection.getUser() + " logged out.");
        connection.setUser(null);
        return res;
    }

	@Override
	public Response delete(Request req) {
		// TODO Auto-generated method stub
        throw new RuntimeException("Not implemented!");
	}
}
