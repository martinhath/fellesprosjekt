package org.fellesprosjekt.gruppe24.common.models;

public class User extends Entity {

    // The users calendar
  
 
    private String username;
    private String password;
    private String email;
    

    /**
     * Vi må ha en konstruktør som ikke tar inn noen argumenter
     * pga. Kryonet. Denne blir kjørt, så vi holder den tom :)
     */
    @SuppressWarnings("unused")
    public User(){}

    public User(int id, String name) {
        super(id, name);
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
