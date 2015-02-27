package org.fellesprosjekt.gruppe24.database;

import java.util.ArrayList;
import java.util.HashMap;

import org.fellesprosjekt.gruppe24.common.models.User;

public class UserDatabaseHandler {

	public static ArrayList<User> getAllUsers() {
		ArrayList<User> allUsers = new ArrayList<User>();
		String query = "SELECT * FROM User;";
		ArrayList<HashMap<String, String>> result = DatabaseManager.getList(query);
		for(HashMap<String, String> hm : result) {
			allUsers.add(generateUser(hm));
		}
		return allUsers;
	}
	
	private static User generateUser(HashMap<String, String> info) {
		//User user = new user(info.get("userid"), info.get("username"), info.get("email"), info.get("password"),
		//		info.get("create_time"), info.get("update_time"));
		// TODO: Add proper constructor so the previous lines can be uncommented.
		User user = new User(Integer.parseInt(info.get("userid")), info.get("username"));
		return user;
	}
	
	public static void addNewUser(User user) {
		//String query = String.format("INSERT INTO User (username, email, password, create_time) VALUES"
		//		+ " ('%s', '%s', '%s', CURRENT_TIMESTAMP)", user.getUsername());
		// TODO: 
		String query = String.format("INSERT INTO User (username, email, password, create_time) VALUES"
				+ " ('%s', '%s', '%s', CURRENT_TIMESTAMP)", user.getUsername(), "sd", "ll");
		DatabaseManager.updateQuery(query);
	}
	
	public static void updateUser(User user) {
		//String query = String.format("UPDATE User SET username = ", args)
	}
	
	public static void main(String[] args) {
		UserDatabaseHandler.getAllUsers();
	}
	
}
