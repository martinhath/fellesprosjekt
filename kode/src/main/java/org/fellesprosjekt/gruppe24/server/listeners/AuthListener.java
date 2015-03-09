package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.AuthController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class AuthListener extends ServerListener {

    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof AuthRequest))
            return;
        ServerController controller = new AuthController(conn);
        switch(req.type){
            case POST:
                controller.post(req);
                break;
            case PUT:
                controller.put(req);
                break;
            case GET:
                controller.get(req);
                break;
        }
    }
}
