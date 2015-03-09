package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.MeetingController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class MeetingListener extends ServerListener{
    @Override
    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof MeetingRequest))
            return;
        ServerController controller = new MeetingController(conn);
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
        }
    }
}
