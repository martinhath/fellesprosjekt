package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.UserController;

public class UserListener extends ServerListener {

    @Override
    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof UserRequest))
            return;
        UserController controller = new UserController(conn);
        switch (req.type) {
            case POST:
                controller.post(req);
                break;
            case PUT:
                controller.put(req);
                break;
            case GET:
                controller.get(req);
                break;
            case LIST:
                controller.list(req);
                break;
            case DELETE:
            	controller.delete(req);
            	break;
        }
    }
}
