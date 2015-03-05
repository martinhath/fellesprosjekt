package org.fellesprosjekt.gruppe24.client.mock;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import org.fellesprosjekt.gruppe24.client.listeners.ClientListener;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.UserRequest;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class MockClient extends Client{
    /**
     Denne klassen kan vi kjøre for å teste request
     og response til serveren uten å trenge å starte
     et GUI.

     Den kan egentlig brukes til testing, fordi Junit
     fungerer ikke så bra med callbacksystemet som er kryonet..
     */

    private Logger logger = Logger.getLogger(getClass().getName());

    public void doStuff() {
        Request req = new UserRequest();
        req.type = Request.Type.LIST;
        logger.info("sender ting");
        sendTCP(req);

        addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection con, Response res) {
                logger.info("får tilbake ting");
                List<User> users = (List<User>) res.payload;
                for (User user : users) {
                    System.out.println(user);
                }
                removeListener(this);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        MockClient client = new MockClient();
        KryoUtils.registerClasses(client.getKryo());
        try {
            client.start();
            client.connect(5000, "127.0.0.1", 9001, 9002);
        } catch (IOException e){
            e.printStackTrace();
        }

        client.doStuff();
        Thread.sleep(1000);
    }
}
