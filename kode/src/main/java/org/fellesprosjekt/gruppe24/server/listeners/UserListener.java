package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.UserController;

public class UserListener extends ServerListener {

    @Override
    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof UserRequest))
            return;
        UserController controller = new UserController(conn);
        Response res;
        switch (req.type) {
            case POST:
                res = controller.post(req);
                break;
            case PUT:
                res = controller.put(req);
                break;
            case GET:
                res = controller.get(req);
                break;
            case LIST:
                res = controller.list(req);
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
