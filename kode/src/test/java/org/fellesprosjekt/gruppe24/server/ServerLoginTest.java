package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ServerLoginTest {
    Logger logger = Logger.getLogger(getClass().getName());

    static CalendarServer server;
    static Client client;

    UserDatabaseHandler uhandler;

    User user;

    @Before
    public void before() throws Exception {
        uhandler = UserDatabaseHandler.GetInstance();
        user = new User("martinhath_9123904", "passord7");
        user.setName("Martin Thoresen");
        user = uhandler.insert(user);
        if (user == null) {
            logger.severe("Failed to insert user.");
            fail();
        }

        int tcp = 6788;
        int udp = 6789;
        if (server == null) {
            server = new CalendarServer(tcp, udp);
            KryoUtils.registerClasses(server.getServer().getKryo());
            server.start();
        }
        if (client == null) {
            client = new Client();
            client.start();
            client.connect(5000, "127.0.0.1", tcp, udp);

            KryoUtils.registerClasses(client.getKryo());
        }
    }

    @After
    public void after() {
        if (!uhandler.delete(user)){
            System.err.println("Failed to delete user " + user);
        }
    }

    @Test
    public void testLoginWithUser() throws Exception {
        LoginInfo loginInfo = new LoginInfo(
                "martinhath", "marinerkul");
        Request req = new AuthRequest(Request.Type.POST,
                AuthRequest.Action.LOGIN, loginInfo);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    User user = (User) res.payload;
                    assertNotNull(user);
                    assertEquals(user.getUsername(), "martinhath");
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

    @Test
    public void testLoginWithoutUser() throws Exception {
        LoginInfo loginInfo = new LoginInfo(
                "jegfinnesikke", "martinerkul");
        Request req = new AuthRequest(Request.Type.POST,
                AuthRequest.Action.LOGIN, loginInfo);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(req.type, Response.Type.FAIL);
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

}
