package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.TestInitRunner;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.Room;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.common.models.net.RoomRequest;
import org.fellesprosjekt.gruppe24.database.RoomDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(TestInitRunner.class)
public class ServerRoomTest extends TestCase {

    static CalendarServer server;
    static Client client;
    User viktor;
    User martin;
    User herman;
    Meeting meeting1;
    Meeting meeting2;
    Room room1;
    RoomDatabaseHandler rhandler;
    UserDatabaseHandler uhandler;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        uhandler = UserDatabaseHandler.GetInstance();
        rhandler = RoomDatabaseHandler.GetInstance();
        viktor = uhandler.getUserFromUsername("Viktor");
        room1 = rhandler.get(1);

        int tcp = 6788;
        int udp = 6789;

        if (server == null) {
            server = new CalendarServer(tcp, udp);
        }

        if (client == null) {
            client = new Client();
            client.start();
            client.connect(5000, "127.0.0.1", tcp, udp);

            KryoUtils.registerClasses(client.getKryo());
        }
    }

    @Test
    public void testGetRoomsForMeeting() throws Exception {
        Request req = new RoomRequest(Request.Type.LIST, meeting1);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    List<Room> rooms = (ArrayList<Room>) res.payload;
                    assertNotNull(rooms);
                    assertTrue(rooms.contains(room1));
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }
}
