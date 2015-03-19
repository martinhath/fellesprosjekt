package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.GroupRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.GroupController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class GroupListener extends ServerListener {

    @Override
    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof GroupRequest))
            return;
        ServerController controller = new GroupController(conn);
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
