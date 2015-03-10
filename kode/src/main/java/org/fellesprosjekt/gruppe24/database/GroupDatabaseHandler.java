package org.fellesprosjekt.gruppe24.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.GroupNotification;
import org.fellesprosjekt.gruppe24.common.models.Meeting;
import org.fellesprosjekt.gruppe24.common.models.User;

public class GroupDatabaseHandler extends DatabaseHandler<Group> {

    private static Logger lgr = Logger.getLogger(MeetingDatabaseHandler.class.getName());
    private static GroupDatabaseHandler instance;

    public static GroupDatabaseHandler GetInstance() {
        if (instance == null)
            instance = new GroupDatabaseHandler();
        return instance;
    }

    public List<Group> getAllGroups(String query) {
        List<Group> groups = new ArrayList<Group>();
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
        for (HashMap<String, String> row : result) {
            groups.add(generateGroup(row));
        }
        return groups;
    }

    public List<Group> getAllGroupsForUser(User user) {
        String query = String.format("SELECT ug.* FROM User_group_has_User AS ughu JOIN User_group AS ug "
                + "ON ug.groupid = ughu.User_group_groupid WHERE ughu.User_userid = %s;", user.getId());
        return getAllGroups(query);
    }

    public Group generateGroup(HashMap<String, String> info) {
        Group group = new Group(Integer.parseInt(info.get("groupid")), info.get("name"), Integer.parseInt(info.get("owner_id")));
        return group;
    }
    
    public GroupNotification generateGroupNotification(User user, HashMap<String, String> info) {
    	boolean read = info.get("notification_read").equals("1") ? true : false;
    	boolean confirmed = info.get("confirmed").equals("1") ? true : false;
    	GroupNotification gn = new GroupNotification(user, info.get("notification_message"), 
    	read, confirmed, generateGroup(info));
    	return gn;
    }

    public static void main(String[] args) {
        //GroupDatabaseHandler.getAllGroupsForUser(new User(1, "Herman", "email"));
    }

    public List<User> getAllUsersInGroup(Group group) {
        String query = String.format("SELECT u.* FROM User_group_has_User AS ughu JOIN User AS u "
                + "ON u.userid = ughu.User_userid WHERE ughu.User_group_groupid = %s;", group.getId());
        return UserDatabaseHandler.GetInstance().getAllUsers(query);
    }

    public List<GroupNotification> getAllGroupInvites(User user) {
        List<GroupNotification> invites = new ArrayList<GroupNotification>();
        String query = String.format("SELECT ughu.*, ug.name, ug.owner_id FROM User_group_has_User AS ughu JOIN User_group AS ug "
                + "ON ug.groupid = ughu.User_group_groupid WHERE ughu.User_userid = %s;", user.getId());
        ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
        for(HashMap<String, String> row : result) {
			invites.add(generateGroupNotification(user, row));
        }
        return invites;
    }
    
    public void setInviteConfirmation(int userid, int groupid, boolean confirmed) {
		int b = confirmed ? 1 : 0;
		String query = String.format("UPDATE User_group_has_User SET confirmed = %s WHERE User_userid = %s AND "
				+ "User_group_groupid = %s", b, userid, groupid);
		DatabaseManager.updateQuery(query);
	}
	
	public void addUserToGroup(User user, Group group, String message) {
		try {
			lgr.log(Level.INFO, String.format("Trying to add User (userid: %s) to User_group (groupid: %s)", 
					user.getId(), group.getId())); 
			String query = "INSERT INTO User_group_has_User (User_userid, User_group_groupid, notification_message)"
					+ " VALUES (?, ?, ?);";
			PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
			if(message == null || message.equalsIgnoreCase(""))
				message = String.format("Du har blitt invitert til gruppen '%s'", group.getName());
			ps.setInt(1, user.getId());
			ps.setInt(2, group.getId());
			ps.setString(3, message);
			DatabaseManager.executePS(ps);
		} catch (Exception ex) {
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	public void removeUserFromGroup(int userid, int groupid) {
		String query = String.format("DELETE FROM User_group_has_user WHERE User_userid = %s AND User_group_groupid = %s;");
		DatabaseManager.updateQuery(query);
	}
	
	public List<Meeting> getAllMeetingsForGroup(int groupid) {
		List<Meeting> meetings = new ArrayList<Meeting>();
		String query = "SELECT * FROM Meeting WHERE Group_groupid = " + groupid + ";";
		ArrayList<HashMap<String, String>> result =	DatabaseManager.getList(query);
		for (HashMap<String, String> row : result) {
			meetings.add(MeetingDatabaseHandler.GetInstance().generateMeeting(row));
		}
		return meetings;
	}

    @Override
    public Group insert(Group group) {
        try {
            lgr.log(Level.INFO, "Trying to insert new Group: " + group.toString());
            String query = "INSERT INTO User_group (name, owner_id, create_time) VALUES "
                    + "(?, ?, NOW());";
            PreparedStatement ps = DatabaseManager.getPreparedStatement(query);
            ps.setString(1, group.getName());
            ps.setInt(2, group.getOwnerId());
            int id = DatabaseManager.executePS(ps);
            Group g = new Group(id, group.getName(), group.getOwnerId());
            return g;
        } catch (Exception ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public Group get(int id) {
        lgr.log(Level.INFO, "Trying to get Group by id: " + id);
        String query = "SELECT * FROM User_group WHERE groupid = " + id + ";";
        try {
            HashMap<String, String> row = DatabaseManager.getRow(query);
            lgr.log(Level.INFO, "Trying to generate Group from: " + row.toString());
            return generateGroup(row);
        } catch (SQLException ex) {
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    public List<Group> getAll() {
        String query = "SELECT * FROM User_group";
        return getAllGroups(query);
    }

    @Override
    public boolean delete(Group group) {
        String query = "DELETE FROM User_group WHERE groupid = " + group.getId() + ";";
        DatabaseManager.updateQuery(query);
        return true;
    }

    @Override
    public boolean update(Group group) {
        String query = String.format("UPDATE User_group SET name = '%s', owner_id = %s WHERE groupid = %s",
                group.getName(), group.getOwnerId(), group.getId());
        DatabaseManager.updateQuery(query);
        return true;
    }
}
