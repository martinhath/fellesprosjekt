package org.fellesprosjekt.gruppe24.common.models;

import java.util.List;

public class User {

    private String name;
    private Calendar calendar;

    private List<Group> groups;

    /**
     * Vi må ha en konstruktør som ikke tar inn noen argumenter
     * pga. Kryonet. Denne blir kjørt, så vi holder den tom :)
     */
    @SuppressWarnings("unused")
    public User(){}

    public User(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
