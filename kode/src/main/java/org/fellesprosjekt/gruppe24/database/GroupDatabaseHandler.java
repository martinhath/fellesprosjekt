package org.fellesprosjekt.gruppe24.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.GroupNotification;
import org.fellesprosjekt.gruppe24.common.models.User;

public class GroupDatabaseHandler {
	
	public static List<Group> getAllGroups(String query) {
		List<Group> groups = new ArrayList<Group>();
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> row : result) {
			groups.add(generateGroup(row));
		}
		return groups;
	}
	
	public static List<Group> getAllGroups() {
		String query = "SELECT * FROM User_group";
		return getAllGroups(query);
	}

	public static List<Group> getAllGroupsForUser(User user) {
		String query = String.format("SELECT ug.* FROM User_group_has_User AS ughu JOIN User_group AS ug "
				+ "ON ug.groupid = ughu.User_group_groupid WHERE ughu.User_userid = %s;", user.getId());
		return getAllGroups(query);
	}
	
	private static Group generateGroup(HashMap<String, String> info) {
		//User user = new user(info.get("userid"), info.get("username"), info.get("email"), info.get("password"),
		//		info.get("create_time"), info.get("update_time"));
		// TODO: Add proper constructor so the previous lines can be uncommented.
		Group group = new Group(Integer.parseInt(info.get("groupid")), info.get("name"));
		return group;
	}
	
	public static void main(String[] args) {
		GroupDatabaseHandler.getAllGroupsForUser(new User(1, "Herman", "email"));
	}
	
	public static List<User> getAllUsersInGroup(Group group) {
		String query = String.format("SELECT u.* FROM User_group_has_User AS ughu JOIN User AS u "
				+ "ON u.userid = ughu.User_userid WHERE ughu.User_group_groupid = %s;", group.getId());
		return UserDatabaseHandler.getAllUsers(query);
	}
	
	public static List<GroupNotification> getAllGroupInvites(User user) {
		List<GroupNotification> invites = new ArrayList<GroupNotification>();
		String query = String.format("SELECT ughu.*, ug.name, ug.owner_id FROM User_group_has_User AS ughu JOIN User_group AS ug "
				+ "ON ug.groupid = ughu.User_group_groupid WHERE ughu.User_userid = %s;", user.getId());
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> hm : result) {
			System.out.println(hm.get("notification_message"));
		}
		return invites;
	}
	
	public static void updateGroup(Group group) {
		String query = String.format("UPDATE User_group SET name = '%s', owner_id = %s WHERE groupid = %s",
				group.getName(), "TODO", group.getId());
		// TODO: owner_id
		DatabaseManager.updateQuery(query);
	}
	
}
