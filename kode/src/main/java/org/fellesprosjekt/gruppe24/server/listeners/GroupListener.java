package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.GroupRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.GroupController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class GroupListener extends ServerListener {

    @Override
    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof GroupRequest))
            return;
        ServerController controller = new GroupController(conn);
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
