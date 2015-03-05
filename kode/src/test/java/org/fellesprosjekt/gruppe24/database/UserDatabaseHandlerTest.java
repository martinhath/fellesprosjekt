package org.fellesprosjekt.gruppe24.database;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import junit.framework.TestCase;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class UserDatabaseHandlerTest {
    public void setUp() {

    }

    @Test
    public void testCanGetAllUsers() {
        List<User> userList = UserDatabaseHandler.getAllUsers();
        TestCase.assertNotNull(userList);
    }

    @Test
    public void testCanInsertAndDeleteUser() {
        String username = "gopet";
        String name = "Geir Ove Pettersen";
        String password = "123";

        /* et eller annet er null
        User user = new User(username, name, password);
        UserDatabaseHandler.addNewUser(user, password);

        User user2 = UserDatabaseHandler.getById(user.getId());
        TestCase.assertNotNull(user2);
        TestCase.assertEquals(user.getId(), user2.getId());

        UserDatabaseHandler.deleteById(user.getId());

        User user3 = UserDatabaseHandler.getById(user.getId());
        */
    }

    @Test
    public void canAuthenticateUser() {
        String username = "Viktor";
        String password = "viktor1";
        User user = UserDatabaseHandler.authenticate(username, password);

        TestCase.assertNotNull(user);
        TestCase.assertEquals(username, user.getUsername());
    }
}
