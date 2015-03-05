package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.InvitationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.InvitationController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class InvitationListener extends ServerListener {
	
	public void receivedRequest(ServerConnection conn, Request req) {
        ServerController controller;
        if (req instanceof InvitationRequest){
            controller = new InvitationController(conn);
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