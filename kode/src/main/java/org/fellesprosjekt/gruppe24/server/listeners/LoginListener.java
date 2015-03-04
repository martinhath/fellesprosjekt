package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;
import org.fellesprosjekt.gruppe24.server.Listener;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;
import org.fellesprosjekt.gruppe24.server.controllers.UserController;

public class LoginListener extends Listener{

    public void receivedRequest(ServerConnection conn, Request req) {
        ServerController controller;
        if (req instanceof UserRequest){
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
            }
        }
    }
}
