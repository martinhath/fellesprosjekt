package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.fellesprosjekt.gruppe24.TestInitRunner;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.format.TextStyle;
import java.util.logging.Logger;

import static org.junit.Assert.*;

//@RunWith(TestInitRunner.class)
public class ServerLoginTest {
    Logger logger = Logger.getLogger(getClass().getName());

    CalendarServer server = TestInitRunner.server;
    Client client = TestInitRunner.client;

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
