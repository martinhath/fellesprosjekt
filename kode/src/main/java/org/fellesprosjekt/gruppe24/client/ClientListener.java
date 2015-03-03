package org.fellesprosjekt.gruppe24.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientListener extends Listener{
    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void received(Connection conn, Object obj) {
        if (!(obj instanceof Response)) {
            logger.log(Level.WARNING, "Response error: " + obj);
            return;
        }
        Response res = (Response) obj;

        if (res.type == Response.Type.FAIL){
            logger.log(Level.WARNING, "Failure: " + res.payload);
        }
    }
}
