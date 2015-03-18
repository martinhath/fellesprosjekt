package org.fellesprosjekt.gruppe24.client.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListener extends Listener{
    Logger logger = Logger.getLogger(getClass().getName());


    public void receivedRequest(Connection conn, Request req) {

    }

    public void receivedResponse(Connection conn, Response res) {

    }

    protected boolean listInstanceOf(Object obj, Class c) {
        if (!(obj instanceof List)) return false;
        List list = (List) obj;
        // sketchy, men la gå
        if (list.size() == 0) return false;
        Object o = list.get(0);
        if (o == null) return false;
        return o.getClass() == c;
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
