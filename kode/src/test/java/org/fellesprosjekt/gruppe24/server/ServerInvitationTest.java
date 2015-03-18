package org.fellesprosjekt.gruppe24.server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.TestInitRunner;
import org.fellesprosjekt.gruppe24.common.Kryo.KryoUtils;
import org.fellesprosjekt.gruppe24.common.models.Entity;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.common.models.net.InvitationRequest;
import org.fellesprosjekt.gruppe24.common.models.net.Request;
import org.fellesprosjekt.gruppe24.common.models.net.Response;
import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@RunWith(TestInitRunner.class)
public class ServerInvitationTest extends TestCase {

    CalendarServer server = TestInitRunner.server;
    Client client = TestInitRunner.client;

    User martin;
    User viktor;
    User ingrid;
    Meeting meeting1;
    Meeting meeting2;


    @Before
    public void setUp() throws Exception {
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
        meeting1 = MeetingDatabaseHandler.GetInstance().getAll().get(0);
        meeting2 = MeetingDatabaseHandler.GetInstance().getAll().get(1);

        if (meeting1 == null) {
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
        }
        if (meeting2 == null) {
            meeting1 = new Meeting(
                    "Briefing med Albanias ambassadør i Oslo",
                    "er fra test",
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(2),
                    "",
                    new ArrayList<Entity>(),
                    martin);
            MeetingDatabaseHandler.GetInstance().insert(meeting1);
        }
    }

    @Test
    public void testCanAcceptInvitation() {

    }

    @Test
    public void testCanDeclineInvitation() {

    }

    @Test
    public void testCanGetInvitation() {
        HashMap<String, Integer> ids = new HashMap<String, Integer>(2);
        ids.put("meetingId", meeting1.getId());
        ids.put("userId", martin.getId());

        Request req = new InvitationRequest(Request.Type.GET, ids, InvitationRequest.Answer.MAYBE);
        client.sendTCP(req);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);
                try {
                    Response res = (Response) object;
                    assertEquals(res.type, Response.Type.OK);
                } catch (Exception e) {
                    fail();
                }
                client.removeListener(this);
            }
        });
    }

    @Test
    public void testCanGetAllInvitationsOfUser() {

    }
}
