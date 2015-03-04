package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;
import org.fellesprosjekt.gruppe24.server.controllers.UserController;

public class ServerListener extends Listener{

    public void received(Connection connection, Object obj) {
        UserController userController = new UserController((ServerConnection) connection);
        /**
         * Klienten sender en melding nå og da for å ikke
         * miste tilkoblingen.
         */
        if (obj instanceof FrameworkMessage.KeepAlive)
            return;

        // vi kan også få en response ?
        Request req = (Request) obj;

        if (req instanceof UserRequest){
            switch(req.type){
                case POST:
                    userController.post(req);
                    break;
                case PUT:
                    userController.put(req);
                    break;
                case GET:
                    userController.get(req);
                    break;
            }
        }
    }
}
