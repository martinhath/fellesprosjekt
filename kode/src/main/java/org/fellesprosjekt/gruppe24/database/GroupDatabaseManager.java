package org.fellesprosjekt.gruppe24.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.fellesprosjekt.gruppe24.common.models.Group;
import org.fellesprosjekt.gruppe24.common.models.User;

public class GroupDatabaseManager {

	public static ArrayList<Group> getAllGroupsForUser(User user) {
		ArrayList<Group> groups = new ArrayList<Group>();
		String query = String.format("SELECT * FROM User_group AS ug, User AS u, User_group_has_User AS ughu WHERE"
				+ "u.userid = ughu.User_userid AND ug.groupid = ughu.User_group_groupid AND u.userid = %s;", user.getID());
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> hm : result) {
			//groups.add(e)
		}
		return groups;
	}
	
}
