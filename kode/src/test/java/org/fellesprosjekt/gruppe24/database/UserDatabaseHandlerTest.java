package org.fellesprosjekt.gruppe24.database;

import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.junit.Test;

import java.util.List;

/**
 * Created by viktor on 03.03.15.
 */
public class UserDatabaseHandlerTest extends TestCase {
    public void setUp() {

    }

    public void testCanGetAllUsers() {
        List<User> userList = UserDatabaseHandler.GetInstance().getAll();
        TestCase.assertNotNull(userList);
    }

    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteUser() {
        String username = "gopet";
        String name = "Geir Ove Pettersen";
        String password = "123";
        String email = "gopet@ntnu.no";

        User user = new User(username, name, password, email);
        TestCase.assertNotNull(user);


        user = UserDatabaseHandler.GetInstance().insert(user);
        TestCase.assertNotNull(user);

        User user2 = UserDatabaseHandler.GetInstance().getUserFromUsername(user.getUsername());
        TestCase.assertNotNull(user2);
        TestCase.assertEquals(user.getId(), user2.getId());

        UserDatabaseHandler.GetInstance().delete(user);

        User user3 = UserDatabaseHandler.GetInstance().get(user.getId());
    }

    public void canAuthenticateUser() {
        String username = "Viktor";
        String password = "viktor1";
        User user = UserDatabaseHandler.GetInstance().authenticate(username, password);

        TestCase.assertNotNull(user);
        TestCase.assertEquals(username, user.getUsername());
    }

    public void testCanGetAllMeetingsOfUser() {
        User user = UserDatabaseHandler.GetInstance().getAll().get(0);
        List<Meeting> allMeetings = MeetingDatabaseHandler.GetInstance().getAll();
        Meeting meeting = allMeetings.get(0);
        Meeting meeting2 = allMeetings.get(1);

        MeetingDatabaseHandler.GetInstance().addUserToMeeting(meeting, user);
        MeetingDatabaseHandler.GetInstance().addUserToMeeting(meeting2, user);

        List<Meeting> userMeetings = UserDatabaseHandler.GetInstance().getMeetingsOfUser(user);

        for (int i = 0; i < userMeetings.size(); i++) {
            TestCase.assertEquals(allMeetings.get(i).getId(), userMeetings.get(i).getId());
        }
    }

    public void testCanConfirmMeeting() {
        User user = UserDatabaseHandler.GetInstance().getAll().get(0);
        Meeting meeting = MeetingDatabaseHandler.GetInstance().getAll().get(0);

        UserDatabaseHandler.GetInstance().setMeetingConfirmation(user, meeting, true);
    }
}
