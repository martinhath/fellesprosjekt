package org.fellesprosjekt.gruppe24.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;

public class UserDatabaseHandler {
    private static Logger lgr = Logger.getLogger(MeetingDatabaseHandler.class.getName());


    public static List<User> getAllUsers(String query) {
        List<User> allUsers = new ArrayList<User>();
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
        for (HashMap<String, String> hm : result) {
            allUsers.add(generateUser(hm));
        }
        return allUsers;
    }

    public static List<User> getAllUsers() {
        String query = "SELECT * FROM User;";
        return getAllUsers(query);
    }

    private static User generateUser(HashMap<String, String> info) {
        //User user = new user(info.get("userid"), info.get("username"), info.get("email"), info.get("password"),
        //		info.get("create_time"), info.get("update_time"));
        // TODO: Add proper constructor so the previous lines can be uncommented.
        User user = new User(Integer.parseInt(info.get("userid")), info.get("username"), info.get("name"));
        return user;
    }

    /**
     * Inserts a new user into the database
     *
     * @param user     a user object
     * @param password the user's desired password
     */
    public static int addNewUser(User user, String password) {
        //String query = String.format("INSERT INTO User (username, email, password, create_time) VALUES"
        //		+ " ('%s', '%s', '%s', CURRENT_TIMESTAMP)", user.getUsername());
        // TODO:
        //String query = String.format("INSERT INTO User (username, name, email, password, create_time) VALUES"
        //		+ " ('%s', '%s', '%s', CURRENT_TIMESTAMP)", user.getUsername(), user.getName(), "sd", "ll");
        try {
            String query =
                    "INSERT INTO User " +
                            "(username, name, password) " +
                            "VALUES (?, ?, ?)";
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getName());
            ps.setString(3, password);
            return DatabaseManager.executePS(ps);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return -1;
        }
    }

    public static void updateUser(User user) {
        //String query = String.format("UPDATE User SET username = ", args)
    }

    public static User authenticate(String username, String password) {
        String query = String.format("SELECT * FROM User WHERE username=%s AND password=%s", username, password);

        return generateUser(DatabaseManager.getRow(query));
    }

    public static User getById(int id) {
        try {
            lgr.log(Level.INFO, "Trying to get user by id: " + id);
            HashMap<String, String> info = DatabaseManager.getRow(String.format("SELECT * FROM User WHERE userid=%d", id));
            lgr.log(Level.INFO, "Trying to generate user from: " + info.toString());
            return generateUser(info);
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public static boolean deleteById(int id) {
        try {
            lgr.log(Level.INFO, "Trying to delete user by id: " + id);
            DatabaseManager.updateQuery(String.format("DELETE FROM User WHERE userid=%d", id));
            return true;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }



}
