package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;
import org.fellesprosjekt.gruppe24.server.controllers.UserController;

import java.util.logging.Level;

public class UserListener extends ServerListener {

    public void receivedRequest(ServerConnection conn, Request req) {
        ServerController controller;
        if (req instanceof UserRequest) {
            controller = new UserController(conn);
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
                case LIST:
                    controller.list(req);
            }
        }
    }

}
