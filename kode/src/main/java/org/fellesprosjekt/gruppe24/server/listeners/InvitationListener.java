package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.InvitationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.InvitationController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class InvitationListener extends ServerListener {
	
	public void receivedRequest(ServerConnection conn, Request req) {
        ServerController controller;
        if (!(req instanceof InvitationRequest))
            return;

        controller = new InvitationController(conn);
        Response res;
        switch(req.type){
            case POST:
                res = controller.post(req);
                break;
            case PUT:
                res = controller.put(req);
                break;
            case GET:
                res = controller.get(req);
                break;
            case DELETE:
                res = controller.delete(req);
                break;
            default:
                logger.warning(req.toString());
                return;
        }
        conn.sendTCP(res);
    }

}
