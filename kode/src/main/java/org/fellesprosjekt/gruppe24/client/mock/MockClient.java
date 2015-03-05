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

    public void testGetAllUsers() throws InterruptedException {
        Request req = new UserRequest();
        req.type = Request.Type.LIST;
        sendTCP(req);

        addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection con, Response res) {
                List<User> users = (List<User>) res.payload;
                if (users == null)
                    throw new RuntimeException("users == null");
                else
                    logger.info("Test OK.");
                removeListener(this);
            }
        });
        // første test må vente litt lenger, fordi
        // databasen bruker litt tid på første request.
        Thread.sleep(1000);
    }

    public void testGetUser() throws InterruptedException {
        Request req = new UserRequest();
        req.type = Request.Type.GET;
        req.payload = "martinhath";
        sendTCP(req);

        addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection con, Response res) {
                User user = (User) res.payload;
                if (user == null)
                    throw new RuntimeException("user == null");
                else
                    logger.info("Test OK.");
                removeListener(this);
            }
        });
        Thread.sleep(100);
    }

    public void testRegisterUser() throws InterruptedException {
        User user = new User("martinht321", "Martin H. Thoresen", "martinerkul");
        user.setEmail("martin@er.kul");
        Request req = new UserRequest();
        req.type = Request.Type.POST;
        req.payload = user;
        sendTCP(req);

        addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection con, Response res) {
                if (res.type == Response.Type.FAIL)
                    throw new RuntimeException((String) res.payload);
                else
                    logger.info("Test OK.");
                removeListener(this);
            }
        });
        Thread.sleep(100);
    }

    public void testUpdateUser() throws InterruptedException {
        User user = new User("martinhath", "Martin H. Thoresen", "martinerbest");
        user.setEmail("martin@er.kul.no");
        Request req = new UserRequest();
        req.type = Request.Type.PUT;
        req.payload = user;
        sendTCP(req);

        addListener(new ClientListener() {
            @Override
            public void receivedResponse(Connection con, Response res) {
                if (res.type == Response.Type.FAIL)
                    throw new RuntimeException((String) res.payload);
                else
                    logger.info("Test OK.");
                removeListener(this);
            }
        });
        Thread.sleep(100);
    }

    public static void main(String[] args) {
        MockClient client = new MockClient();
        KryoUtils.registerClasses(client.getKryo());
        try {
            client.start();
            client.connect(5000, "127.0.0.1", 9001, 9002);

            client.testGetAllUsers();
            client.testGetUser();
            client.testRegisterUser();
            client.testUpdateUser();
        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
