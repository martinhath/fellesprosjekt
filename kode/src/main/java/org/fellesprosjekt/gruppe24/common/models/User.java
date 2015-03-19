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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String name) {
        super(id, name);
        this.username = username;
    }

    public User(int id, String username, String name, String password, String email) {
        super(id, name);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String name, String password, String email) {
        super(name);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
    	return username;
    }
    
    public String getPassword() {
    	return password;
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
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof User)) return false;
    	return getId() == ((User) obj).getId();
    }
    @Override
    public int hashCode() {
    	return new Integer(getId()).hashCode();
    }
    
    
}
