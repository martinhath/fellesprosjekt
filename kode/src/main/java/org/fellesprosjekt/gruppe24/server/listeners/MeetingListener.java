package org.fellesprosjekt.gruppe24.server.listeners;

import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.server.ServerConnection;
import org.fellesprosjekt.gruppe24.server.controllers.MeetingController;
import org.fellesprosjekt.gruppe24.server.controllers.ServerController;

public class MeetingListener extends ServerListener{
    @Override
    public void receivedRequest(ServerConnection conn, Request req) {
        if (!(req instanceof MeetingRequest))
            return;
        ServerController controller = new MeetingController(conn);
        try {
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
        } catch (ClassCastException e){
            logger.warning(e.toString());
            Response res = Response.GetFailResponse("Illegal type of payload: "
                + req.getClass() + " " + req.payload);
        }
    }
}
