package org.fellesprosjekt.gruppe24.database;

import org.fellesprosjekt.gruppe24.common.models.User;

public class GroupDatabaseManager {

	public static void getAllGroupsForUser(User user) {
		String query = String.format("SELECT * FROM User_group AS ug, User AS u, User_group_has_User AS ughu WHERE"
				+ "u.userid = ughu.User_userid AND ug.groupid = ughu.User_group_groupid AND u.userid = %s;", ""/*user.ID*/);
	}
	
}
