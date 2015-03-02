package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

public class ServerListener extends Listener{

    public void received(Connection connection, Object obj) {
        UserController userController = new UserController((ServerConnection) connection);
        /**
         * Klienten sender en melding nå og da for å ikke
         * miste tilkoblingen.
         */
        if (obj instanceof FrameworkMessage.KeepAlive)
            return;

        Request req = (Request) obj;

        Class model = req.getModel();

        if (model == User.class){
            switch(req.getType()){
                case PUT:
                    userController.put(req);
                    break;
                case GET:
                    userController.get(req);
                    break;
                case AUTH:
                    userController.auth(req);
                    break;
            }
        } else {
            System.err.println("Ukjent klasse: " + model.getCanonicalName());
            Response res = new Response(Response.Type.FAILURE, String.class);
            res.setPayload("Unknown class: "+model.getCanonicalName());
            connection.sendTCP(res);
        }
    }
}
