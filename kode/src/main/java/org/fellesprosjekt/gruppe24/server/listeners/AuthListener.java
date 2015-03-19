package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.AuthController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class AuthListener extends ServerListener {

    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof AuthRequest))
            return;
        ServerController controller = new AuthController(conn);
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
            default:
                logger.warning(req.toString());
                return;
        }
        conn.sendTCP(res);
    }
}
