package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Listener extends com.esotericsoftware.kryonet.Listener{
    Logger logger = Logger.getLogger(getClass().getName());

    public void receivedRequest(ServerConnection conn, Request req) {
    }

    public void receivedResponse(ServerConnection conn, Response res) {
    }

    public void received(ServerConnection conn, Object obj){
        if (obj instanceof Request) {
            receivedRequest(conn, (Request) obj);
        } else if (obj instanceof Response) {
            receivedResponse(conn, (Response) obj);
        } else{
            logger.log(Level.WARNING, "Fikk hverken Request eller Response: " + obj);
        }
    }

    @Override
    public void received(Connection connection, Object obj) {
        if (obj instanceof FrameworkMessage.KeepAlive)
            return;

        try {
            ServerConnection con = (ServerConnection) connection;
            received(con, obj);
        } catch (Exception e){
            logger.log(Level.WARNING, "Fikk en rar melding:");
            logger.log(Level.WARNING, "Connection: " + connection);
            logger.log(Level.WARNING, "Object: " + obj);
        }
    }
}
