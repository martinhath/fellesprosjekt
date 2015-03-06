package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;

public class ServerLoginTest extends TestCase {

    static CalendarServer server;
    static Client client;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        User martin = new User("martinhath", "Martin Thoresen", "", "");
        UserDatabaseHandler.GetInstance().delete(martin);
        UserDatabaseHandler.GetInstance().addNewUser(martin, "martinerkul");

        int tcp = 6788;
        int udp = 6789;

        if (server == null) {
            server = new CalendarServer(tcp, udp);
            server.start();
        }

        if (client == null) {
            client = new Client();
            client.start();
            client.connect(5000, "127.0.0.1", tcp, udp);

            KryoUtils.registerClasses(client.getKryo());
        }
    }

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
