package org.fellesprosjekt.gruppe24.common.models;

public class LoginInfo {
    /**
     * Vi 'trenger' denne klassen for å sende login-info
     * til serveren.
     */

    private String username;
    private String password;

    public LoginInfo(){}

    public LoginInfo(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
