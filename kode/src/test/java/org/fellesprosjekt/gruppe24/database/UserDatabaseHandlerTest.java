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
        List<User> userList = UserDatabaseHandler.getAllUsers();
        TestCase.assertNotNull(userList);
    }

    @Test(expected = java.sql.SQLException.class)
    public void testCanInsertAndDeleteUser() {
        String username = "gopet";
        String name = "Geir Ove Pettersen";
        String password = "123";

        User user = new User(username, name, password);

        User user2 = UserDatabaseHandler.getById(user.getId());
        TestCase.assertNotNull(user2);
        TestCase.assertEquals(user.getId(), user2.getId());

        UserDatabaseHandler.deleteById(user.getId());

        User user3 = UserDatabaseHandler.getById(user.getId());
    }

    public void canAuthenticateUser() {
        String username = "Viktor";
        String password = "viktor1";
        User user = UserDatabaseHandler.authenticate(username, password);

        TestCase.assertNotNull(user);
        TestCase.assertEquals(username, user.getUsername());
    }

    public void testCanGetAllMeetingsOfUser() {
        User user = UserDatabaseHandler.getAllUsers().get(0);
        List<Meeting> allMeetings = MeetingDatabaseHandler.getAllMeetings();
        Meeting meeting= allMeetings.get(0);
        Meeting meeting2 = allMeetings.get(1);

        MeetingDatabaseHandler.addUserToMeeting(meeting, user);
        MeetingDatabaseHandler.addUserToMeeting(meeting2, user);

        List<Meeting> userMeetings = UserDatabaseHandler.getMeetingsOfUser(user);

        for (int i = 0; i < userMeetings.size(); i++) {
            TestCase.assertEquals(allMeetings.get(i).getId(), userMeetings.get(i).getId());
        }
    }
}
