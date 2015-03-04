package org.fellesprosjekt.gruppe24.client.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListener extends Listener{
    Logger logger = Logger.getLogger(getClass().getName());


    public void receivedRequest(Connection conn, Request req) {

    }

    public void receivedResponse(Connection conn, Response res) {

    }

    @Override
    public void received(Connection conn, Object obj) {
        if (obj instanceof FrameworkMessage.KeepAlive){
            return;
        } else if (obj instanceof Request) {
            receivedRequest(conn, (Request) obj);
        } else if (obj instanceof Response) {
            receivedResponse(conn, (Response) obj);
        } else {
            logger.log(Level.WARNING, "Fikk hverken Request eller Response: " + obj);
        }
    }
}
