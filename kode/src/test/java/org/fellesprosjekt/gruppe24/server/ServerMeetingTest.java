package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.LoginInfo;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.AuthRequest;
import org.fellesprosjekt.gruppe24.common.models.net.MeetingRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerMeetingTest extends TestCase {

    static CalendarServer server;
    static Client client;
    User martin;
    User viktor;
    User ingrid;
    Meeting meeting1;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        martin = UserDatabaseHandler.GetInstance().getUserFromUsername("Martin");
        viktor = UserDatabaseHandler.GetInstance().getUserFromUsername("Viktor");
        ingrid = UserDatabaseHandler.GetInstance().getUserFromUsername("Ingrid");
        if (martin == null) {
            martin = new User("martinhath", "Martin Thoresen", "", "");
            martin = UserDatabaseHandler.GetInstance().insert(martin);
        }
        if (viktor == null) {
            viktor = new User("viktorfa", "Viktor Andersen", "", "");
            viktor = UserDatabaseHandler.GetInstance().insert(viktor);
        }
        if (ingrid == null) {
            ingrid = new User("ingridvold", "Ingrid Vold", "", "");
            ingrid = UserDatabaseHandler.GetInstance().insert(ingrid);
        }
        meeting1 = new Meeting(
                "Videokonferanse",
                "er fra test",
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "",
                new ArrayList<Entity>(),
                martin);
        MeetingDatabaseHandler.GetInstance().insert(meeting1);

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

    public void testGetMeeting() throws Exception {
        Request req = new MeetingRequest(MeetingRequest.Type.GET, meeting1.getId());
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    Meeting resMeeting = (Meeting) res.payload;
                    assertNotNull(resMeeting);
                    assertEquals(meeting1.getId(), resMeeting.getId());
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

    public void testUpdateMeeting() throws Exception {
        String expectedDesc = "Dette blir et hyggelig møte.";
        meeting1.setDescription(expectedDesc);
        Request req = new MeetingRequest(MeetingRequest.Type.PUT, meeting1);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    Meeting resMeeting = (Meeting) res.payload;
                    assertNotNull(resMeeting);
                    assertEquals(meeting1.getId(), resMeeting.getId());
                    assertEquals(expectedDesc, resMeeting.getDescription());
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

    public void testCreateMeeting() throws Exception {
        String expectedDesc = "sjekker om man kan lage møte";
        Meeting meeting2 = new Meeting(
                "Videokonferanse",
                expectedDesc,
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "",
                new ArrayList<Entity>(),
                martin);
        Request req = new MeetingRequest(MeetingRequest.Type.POST, meeting2);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    Meeting resMeeting = (Meeting) res.payload;
                    assertNotNull(resMeeting);
                    assertEquals(meeting1.getId(), resMeeting.getId());
                    assertEquals(expectedDesc, resMeeting.getDescription());
                    assertEquals(expectedDesc, MeetingDatabaseHandler.GetInstance().get(resMeeting.getId()).getDescription());
                    assertNotNull(MeetingDatabaseHandler.GetInstance().get(resMeeting.getId()));
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }
    public void testListAllMeetings() throws Exception {
        List<Meeting> expected = MeetingDatabaseHandler.GetInstance().getAll();
        Request req = new MeetingRequest(MeetingRequest.Type.LIST, null);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    @SuppressWarnings("unchecked")
                    List<Meeting> resMeetings = (List<Meeting>) res.payload;
                    assertNotNull(resMeetings);
                    assertTrue(resMeetings.contains(meeting1));
                    assertTrue(resMeetings.containsAll(expected));
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }
    public void testListAllMeetingsOfUser() throws Exception {
        Meeting meeting2 = new Meeting(
                "Videokonferanse",
                "",
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "",
                new ArrayList<Entity>(),
                viktor);
        MeetingDatabaseHandler.GetInstance().addUserToMeeting(meeting1, martin, "");
        MeetingDatabaseHandler.GetInstance().addUserToMeeting(meeting2, martin, "");
        List<Meeting> expected = UserDatabaseHandler.GetInstance().getMeetingsOfUser(martin);
        Request req = new MeetingRequest(MeetingRequest.Type.LIST, MeetingRequest.Handler.USER, martin);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    @SuppressWarnings("unchecked")
                    List<Meeting> resMeetings = (List<Meeting>) res.payload;
                    assertNotNull(resMeetings);
                    assertTrue(resMeetings.containsAll(expected));
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

    public void testCreateMeetingNotifications() throws Exception {
        List<Entity> expectedParticipants = new ArrayList<Entity>(2);
        expectedParticipants.add(viktor);
        expectedParticipants.add(ingrid);
        Meeting meeting2 = new Meeting(
                "Videokonferanse",
                "",
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "",
                expectedParticipants,
                martin);
        Request req = new MeetingRequest(MeetingRequest.Type.POST, meeting2);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    Meeting resMeeting = (Meeting) res.payload;
                    assertNotNull(resMeeting);
                    assertTrue(expectedParticipants.containsAll(MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(resMeeting)));
                    assertTrue(UserDatabaseHandler.GetInstance().getMeetingsOfUser(viktor).contains(resMeeting));
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }
    public void testUpdateMeetingNotifications() throws Exception {
        List<Entity> firstParticipants = new ArrayList<Entity>(2);
        List<Entity> expectedParticipants = firstParticipants;
        firstParticipants.add(viktor);
        firstParticipants.add(ingrid);
        firstParticipants.add(martin);
        Meeting meeting2 = new Meeting(
                "Videokonferanse",
                "",
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "",
                firstParticipants,
                martin);
        expectedParticipants.remove(ingrid);
        Meeting meetingBefore = MeetingDatabaseHandler.GetInstance().insert(meeting2);
        Request req = new MeetingRequest(MeetingRequest.Type.PUT, meeting2);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                    Meeting resMeeting = (Meeting) res.payload;
                    assertNotNull(resMeeting);
                    assertFalse(MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(resMeeting).contains(ingrid));
                    assertTrue(MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(resMeeting).contains(viktor));
                    assertTrue(MeetingDatabaseHandler.GetInstance().getUsersOfMeeting(resMeeting).contains(martin));
                    assertTrue(UserDatabaseHandler.GetInstance().getMeetingsOfUser(viktor).contains(resMeeting));
                    assertFalse(UserDatabaseHandler.GetInstance().getMeetingsOfUser(ingrid).contains(resMeeting));
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

}
