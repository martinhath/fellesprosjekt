package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserDatabaseHandlerTest {

    private UserDatabaseHandler uhandler;
    private MeetingDatabaseHandler mhandler;

    User user;

    @Before
    public void before() {
        uhandler = UserDatabaseHandler.GetInstance();
        mhandler = MeetingDatabaseHandler.GetInstance();

        user = new User("Viktor_124", "viktor1");
        user.setName("Viktor Andersen");
        uhandler.insert(user);
    }

    @After
    public void after() {
        if (!uhandler.delete(user)){
            System.err.println("Failed to delete " + user);
        }
    }

    @Test
    public void testCanGetAllUsers() {
        List<User> userList = uhandler.getAll();
        assertNotNull(userList);
    }

    @Test
    public void testCanInsertAndDeleteUser() {
        String username = "gopet";
        String name = "Geir Ove Pettersen";
        String password = "123";
        String email = "gopet@ntnu.no";

        User user = new User(username, name, password, email);
        TestCase.assertNotNull(user);

        user = uhandler.insert(user);
        TestCase.assertNotNull(user);

        User user2 = uhandler.getUserFromUsername(user.getUsername());
        TestCase.assertNotNull(user2);
        TestCase.assertEquals(user.getId(), user2.getId());

        uhandler.delete(user);

        assertNull(uhandler.get(user.getId()));
    }

    @Test
    public void canAuthenticateUser() {
        String username = "Viktor";
        String password = "viktor1";
        User user = uhandler.authenticate(username, password);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testCanGetAllMeetingsOfUser() {
        User user = uhandler.getAll().get(0);
        List<Meeting> allMeetings = mhandler.getAll();
        Meeting meeting = allMeetings.get(0);
        Meeting meeting2 = allMeetings.get(1);

        mhandler.addUserToMeeting(meeting, user);
        mhandler.addUserToMeeting(meeting2, user);

        List<Meeting> userMeetings = uhandler.getMeetingsOfUser(user);

        /*
        Her antar vi at møtene kommer ut i samme rekkefølge, og at
        brukeren skal være med på alle møtene?
        for (int i = 0; i < userMeetings.size(); i++) {
            TestCase.assertEquals(allMeetings.get(i).getId(), userMeetings.get(i).getId());
        }
         */
    }

    @Test
    public void testCanConfirmMeeting() {
        User user = uhandler.getAll().get(0);
        Meeting meeting = mhandler.getAll().get(0);

        uhandler.setMeetingConfirmation(user, meeting, true);
    }
}
