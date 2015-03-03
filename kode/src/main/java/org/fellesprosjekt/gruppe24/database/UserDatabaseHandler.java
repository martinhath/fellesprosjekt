package org.fellesprosjekt.gruppe24.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;

import javax.xml.crypto.Data;

public class UserDatabaseHandler extends DatabaseHandler<User> {

    private static Logger lgr = Logger.getLogger(MeetingDatabaseHandler.class.getName());
    private static UserDatabaseHandler instance;
    
	public static UserDatabaseHandler GetInstance() {
		if(instance == null)
			instance = new UserDatabaseHandler();
		return instance;
	}

    private User generateUser(HashMap<String, String> info) {
		try {
			lgr.log(Level.INFO, "Generating user based on: " + info);
			User user = new User(Integer.parseInt(info.get("userid")), info.get("username"), info.get("name"),
					info.get("password"), info.get("email"));
			return user;
		} catch (Exception ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return null;
		}
	}
    /**
     * Inserts a new user into the database
     *
     * @param user     a user object
     * @param password the user's desired password
     */
    public static int addNewUser(User user, String password) {
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

    public User authenticate(String username, String password) {
        String query = String.format("SELECT * FROM User WHERE username=\"%s\" AND password=\"%s\"", username, password);
        try{
            return generateUser(DatabaseManager.getRow(query));
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

	public List<User> getAllUsers(String query) {
		List<User> allUsers = new ArrayList<User>();
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> row : result) {
			allUsers.add(generateUser(row));
		}
		return allUsers;
	}

	public User getUserFromUsername(String username) {
		String query = "SELECT * FROM User WHERE username = '" + username + "';";
		try {
			HashMap<String, String> row = DatabaseManager.getRow(query);
			return generateUser(row);
		} catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return null;
		}
	}

	@Override
	public User insert(User user) {
		try {
			String query =
				"INSERT INTO User " +
	            "(username, name, password, email, create_time) " +
	            "VALUES (?, ?, ?, ?, NOW());";
	        PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
	        ps.setString(1, user.getUsername());
	        ps.setString(2, user.getName());
	        ps.setString(3, user.getPassword());
	        ps.setString(4, user.getEmail());
	        int id = DatabaseManager.executePS(ps);
	        User u = new User(id, user.getUsername(), user.getName(), user.getPassword(), user.getEmail());
	        return u;
		} catch (Exception ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
	        return null;
	    }
	}

	@Override
	public User get(int id) {
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

	@Override
	public List<User> getAll() {
		String query = "SELECT * FROM User;";
		return getAllUsers(query);
	}

	@Override
	public boolean delete(User user) {
		try {
			lgr.log(Level.INFO, "Trying to delete user by id: " + user.getId());
			DatabaseManager.updateQuery(String.format("DELETE FROM User WHERE userid=%d", user.getId()));
			return true;
		} catch (Exception ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return false;
		}
	}

	@Override
	public boolean update(User user) {
		String query = String.format("UPDATE User SET username = '%s', name = '%s', password = '%s', email = '%s'"
				+ " WHERE userid = %s;", user.getUsername(), user.getName(), user.getPassword(), user.getEmail(), user.getId());
		DatabaseManager.updateQuery(query);
		return true;
	}
    public static List<Meeting> getMeetingsOfUser(User user) {
        List<Meeting> result = new ArrayList<>();
        try {
            for (int meetingid : selectMeetingIDsOfUser(user.getId())) {
                result.add(MeetingDatabaseHandler.getById(meetingid));
            }
            return result;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return result;
        }
    }

    private static List<Integer> selectMeetingIDsOfUser(int userid) throws SQLException {
        List<Integer> result = new ArrayList<>();
        String query = String.format("SELECT Meeting_meetingid FROM User_invited_to_meeting WHERE User_userid=%d", userid);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row: resultSet) {
            result.add(Integer.parseInt(row.get("Meeting_meetingid")));
        }
        return result;
    }

    public static void confirmMeeting(User user, Meeting meeting) {
        try {
            lgr.log(Level.INFO, String.format("Confirming User %s coming to Meeting %s", user.getUsername(), meeting.getName()));
            DatabaseManager.updateField(
                    user.getId(), meeting.getId(), "User_invited_to_meeting", "confirmed", "user", "meeting", true);
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
