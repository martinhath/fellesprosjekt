package org.fellesprosjekt.gruppe24.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;
import org.fellesprosjekt.gruppe24.server.PasswordCryptography;

public class UserDatabaseHandler extends DatabaseHandler<User> {

    private static Logger lgr = Logger.getLogger(UserDatabaseHandler.class.getName());
    private static UserDatabaseHandler instance;
    
	public static UserDatabaseHandler GetInstance() {
		if(instance == null)
			instance = new UserDatabaseHandler();
		return instance;
	}

    private User generateUser(HashMap<String, String> info) {
        String id = info.get("userid");
		String username = info.get("username");
		String name = info.get("name");
		String password = info.get("password");
		String email = info.get("email");
        User user = new User(Integer.parseInt(id), username, name, password, email);
        return user;
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
            return -1;
        }
    }

    public User authenticate(String username, String password) {
        String query = String.format("SELECT * FROM User WHERE username=\"%s\"", username, password);
        try{
        	HashMap<String, String> row = DatabaseManager.getRow(query);
        	PasswordCryptography pc = new PasswordCryptography(password, row.get("salt"));
        	if(pc.compareHash(row.get("password")))
        		return generateUser(row);
        	else
        		return null;
        } catch (SQLException ex) {
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
			if (id < 0) {
				lgr.severe("Got negative index from DatabaseManager.executePS()");
				return null;
			}
	        return new User(id, user.getUsername(), user.getName(), user.getPassword(), user.getEmail());
		} catch (Exception ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
	        return null;
	    }
	}

	public User get(String username) {
		HashMap<String, String> info;
		try {
			info = DatabaseManager.getRow(String.format(
					"SELECT * FROM User WHERE username='%s'", username));
			if (info.size() == 0)
				return null;
			return generateUser(info);
		} catch (SQLException ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return null;
		} catch (NullPointerException e){
			lgr.info(String.format("User '%s' not found.", username));
			return null;
		}
	}

	@Override
	public User get(int id) {
		try {
            HashMap<String, String> info = DatabaseManager.getRow(
					String.format("SELECT * FROM User WHERE userid=%d", id));
			if (info.size() == 0)
				return null;
            return generateUser(info);
        } catch (SQLException ex) {}
		return null;
	}

	@Override
	public List<User> getAll() {
		String query = "SELECT * FROM User;";
		return getAllUsers(query);
	}

	@Override
	public boolean delete(User user) {
		int id = user.getId();
		System.out.println("Deleting user " + id);
		try {
			if (id < 0) // id er ikke satt
				return DatabaseManager.updateQuery(
						String.format("DELETE FROM User WHERE username='%s'", user.getUsername()));
			else
				return DatabaseManager.updateQuery(
						String.format("DELETE FROM User WHERE userid='%d'", id));
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

    public List<Meeting> getMeetingsOfUser(User user) {
        List<Meeting> result = new ArrayList<>();
        try {
            for (int meetingid : selectMeetingIDsOfUser(user.getId())) {
                result.add(MeetingDatabaseHandler.GetInstance().get(meetingid));
            }
            return result;
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return result;
        }
    }

    private List<Integer> selectMeetingIDsOfUser(int userid) throws SQLException {
        List<Integer> result = new ArrayList<>();
        String query = String.format("SELECT Meeting_meetingid FROM User_invited_to_meeting WHERE User_userid=%d", userid);
        ArrayList<HashMap<String, String>> resultSet = DatabaseManager.getList(query);
        for (HashMap<String, String> row: resultSet) {
            result.add(Integer.parseInt(row.get("Meeting_meetingid")));
        }
        return result;
    }

    public void setMeetingConfirmation(User user, Meeting meeting, boolean confirm) {
        try {
            DatabaseManager.updateField(
                    user.getId(), meeting.getId(), "User_invited_to_meeting", "confirmed", "user", "meeting", confirm);
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
