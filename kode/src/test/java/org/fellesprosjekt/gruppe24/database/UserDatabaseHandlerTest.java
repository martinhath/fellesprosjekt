package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class UserDatabaseHandlerTest {

    private Logger logger = Logger.getLogger(getClass().getName());

    private UserDatabaseHandler uhandler;
    private MeetingDatabaseHandler mhandler;

    @Before
    public void before() {
        uhandler = UserDatabaseHandler.GetInstance();
        mhandler = MeetingDatabaseHandler.GetInstance();
    }

    @Test
    public void testCanGetAllUsers() {
        List<User> userList = uhandler.getAll();
        assertNotNull(userList);
    }

    @Test
    public void testFailToGetUserById() {
        User user = uhandler.get(98123);
        assertNull(user);
    }

    @Test
    public void testCanInsertUser() {
        User user = new User("brukernavn", "navn", "password", "e-mail@lel.no");
        User ret = uhandler.insert(user);
        assertNotNull(ret);

        boolean isinlist = false;
        List<User> list = uhandler.getAll();
        for (User u: list) {
            if (user.getUsername() == u.getUsername())
                isinlist = true;
        }
        if (!isinlist){
            fail();
        }
    }

    @Test
    public void testCanStressDB() {
        for (int i = 0; i < 100; i++) {
            uhandler.get(i);
        }
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User("username1"+i, "passowrd");
            users.add(uhandler.insert(user));
        }
        for (int i = 0; i < 100; i++) {
            uhandler.getAll();
        }
        uhandler.delete(users.get(0));
        uhandler.delete(users.get(1));
        uhandler.delete(users.get(2));

        for (int i = 0; i < 100; i++) {
            uhandler.delete(users.get(i));
        }
    }

    @Test
    public void testCanDeleteMissingUser() {
        boolean ret = uhandler.delete(new User(9876, "fail", "lel"));
        assertTrue(ret);
    }

    @Test
    public void testCanInsertAndDeleteUser() {
        User user = new User("brukernavn", "navn", "password", "e-mail@lel.no");

        User ret = uhandler.insert(user);
        assertNotNull(ret);

        if (!uhandler.delete(ret))
            fail();

        ret = uhandler.get(ret.getId());
        assertNull(ret);
    }

    @Test
    public void testCanGetUserByUsername() {
        User user = uhandler.get("Martin");
        assertNotNull(user);
    }

    @Test
    public void canAuthenticateUser() {
        String username = "Viktor";
        String password = "viktor1";
        User user = uhandler.authenticate(username, password);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    public void testCanGetAllMeetingsOfUser() {
        User user = uhandler.getAll().get(0);
        List<Meeting> allMeetings = mhandler.getAll();
        Meeting meeting = allMeetings.get(0);
        Meeting meeting2 = allMeetings.get(1);

        // mhandler.addUserToMeeting(meeting, user);
        // mhandler.addUserToMeeting(meeting2, user);

        List<Meeting> userMeetings = uhandler.getMeetingsOfUser(user);

        /*
        Her antar vi at møtene kommer ut i samme rekkefølge, og at
        brukeren skal være med på alle møtene?
        for (int i = 0; i < userMeetings.size(); i++) {
            TestCase.assertEquals(allMeetings.get(i).getId(), userMeetings.get(i).getId());
        }
         */
    }

    public void testCanConfirmMeeting() {
        User user = uhandler.getAll().get(0);
        Meeting meeting = mhandler.getAll().get(0);

        uhandler.setMeetingConfirmation(user, meeting, true);
    }
}
