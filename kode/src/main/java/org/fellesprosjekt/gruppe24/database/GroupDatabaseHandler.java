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
		for(HashMap<String, String> hm : result) {
			groups.add(generateGroup(hm));
		}
		return groups;
	}
	
	public static List<Group> getAllGroups() {
		String query = "SELECT * FROM User_group";
		return getAllGroups(query);
	}

	public static List<Group> getAllGroupsForUser(User user) {
		String query = String.format("SELECT * FROM User_group AS ug, User AS u, User_group_has_User AS ughu WHERE "
				+ "u.userid = ughu.User_userid AND ug.groupid = ughu.User_group_groupid AND u.userid = %s;", user.getID());
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
		String query = String.format("SELECT * FROM User AS u, User_group AS ug, User_group_has_User AS ughu WHERE "
				+ "ug.groupid = ughu.User_group_groupid AND u.userid = ughu.User_userid AND ug.groupid = %s;", group.getID());
		return UserDatabaseHandler.getAllUsers(query);
	}
	
	public static List<GroupNotification> getAllGroupInvites(User user) {
		List<GroupNotification> invites = new ArrayList<GroupNotification>();
		String query = String.format("SELECT * FROM User AS u, User_group AS ug, User_group_has_User AS ughu WHERE "
				+ "ughu.User_userid = u.userid AND u.userid = %s;", user.getID());
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> hm : result) {
			System.out.println(hm.get("notification_message"));
		}
		return invites;
	}
	
}
