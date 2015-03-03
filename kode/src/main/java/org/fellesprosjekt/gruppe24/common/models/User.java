package org.fellesprosjekt.gruppe24.common.models;

import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;

public class User extends Entity {

    // The users calendar
  
 
    private String username;
    private String name;
    private String password;
    private String email;
    

    /**
     * Vi må ha en konstruktør som ikke tar inn noen argumenter
     * pga. Kryonet. Denne blir kjørt, så vi holder den tom :)
     */
    @SuppressWarnings("unused")
    public User(){}

    public User(String username, String name, String password) {
        super(name);
        this.username = username;
        this.setId(UserDatabaseHandler.addNewUser(this, password));
    }

    public User(int id, String username, String name) {
        super(id, name);
        this.username = username;
    }

    public String getUsername() {
    	return username;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
}
