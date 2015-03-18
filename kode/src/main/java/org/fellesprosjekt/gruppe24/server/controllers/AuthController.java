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
    public void post(Request r) {
        AuthRequest req = (AuthRequest) r;
        logger.log(Level.INFO, "POST User");
        LoginInfo loginInfo = (LoginInfo) req.payload;

        if (req.action == AuthRequest.Action.LOGIN) {
            login(loginInfo);
        } else if (req.action == AuthRequest.Action.LOGOUT) {
            logout();
        }
    }

    @Override
    public void put(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public void get(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    @Override
    public void list(Request req) {
        throw new RuntimeException("Ikke implmentert!");
    }

    private boolean login(LoginInfo loginInfo){
        Logger.getLogger(getClass().getName()).log(
                Level.INFO, "User login: " + loginInfo);
        if (connection.getUser() != null) {
            connection.sendTCP(Response.GetFailResponse("You are already logged in"));
        }
        Response res = new Response();
        User user = UserDatabaseHandler.GetInstance().authenticate(
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

        logger.info("User " + connection.getUser() + " logged in.");

        connection.sendTCP(res);

        // Sender uleste notifications til brukeren som har logget inn.
        Request req = new NotificationRequest(Request.Type.LIST, false, user);
        ServerController c = new NotificationController(connection);
        c.list(req);
        return true;
    }

    private boolean logout() {
        if (connection.getUser() == null ) {
            connection.sendTCP(
                    Response.GetFailResponse("You are not logged in"));
            return false;
        }
        User user = connection.getUser();

        // Trenger vi å ha med user payloaden?
        Response res = new Response(Response.Type.OK, user);
        logger.info("User " + connection.getUser() + " logged out.");
        connection.setUser(null);
        connection.sendTCP(res);
        return true;
    }

	@Override
	public void delete(Request req) {
		// TODO Auto-generated method stub
        throw new RuntimeException("Not implemented!");
	}
}
