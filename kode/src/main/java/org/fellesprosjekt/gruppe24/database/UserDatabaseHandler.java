package org.fellesprosjekt.gruppe24.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fellesprosjekt.gruppe24.common.models.User;

public class UserDatabaseHandler {
	
	public static List<User> getAllUsers(String query) {
		List<User> allUsers = new ArrayList<User>();
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> hm : result) {
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
		User user = new User(Integer.parseInt(info.get("userid")), info.get("name"), info.get("email"));
		return user;
	}
	
	public static void addNewUser(User user) {
		//String query = String.format("INSERT INTO User (username, email, password, create_time) VALUES"
		//		+ " ('%s', '%s', '%s', CURRENT_TIMESTAMP)", user.getUsername());
		// TODO: 
		String query = String.format("INSERT INTO User (username, name, email, password, create_time) VALUES"
				+ " ('%s', '%s', '%s', CURRENT_TIMESTAMP)", user.getUsername(), user.getName(), "sd", "ll");
		DatabaseManager.updateQuery(query);
	}
	
	public static void updateUser(User user) {
		//String query = String.format("UPDATE User SET username = ", args)
	}
	
	public static void main(String[] args) {
		UserDatabaseHandler.getAllUsers();
	}
	
}
