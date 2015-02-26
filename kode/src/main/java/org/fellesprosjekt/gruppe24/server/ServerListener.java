package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.User;

public class ServerListener extends Listener{

    private Server server;
    private ServerConnection connection;

    private UserController userController;

    public ServerListener(Server server, ServerConnection connection){
        this.server = server;
        this.connection = connection;

        userController = new UserController(connection);
    }

    public void received(Connection connection, Object obj) {
        connection.sendTCP("hallo!!!!!!!!!!!!!!!!!!!!!");
        server.sendToAllTCP("ALLE FÅR DETTE");
        System.out.println("Server received");
        userController.setConnection((ServerConnection) connection);

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
            }
        } else {
            System.err.println("Ukjent klasse: " + model.getCanonicalName());
        }

        connection.sendTCP("halla");
    }
}
