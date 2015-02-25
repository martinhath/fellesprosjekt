package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class User extends Entity {

    // The users calendar
    private Calendar calendar;

    // All groups the user are in
    private List<Group> groups;

    /**
     * Vi må ha en konstruktør som ikke tar inn noen argumenter
     * pga. Kryonet. Denne blir kjørt, så vi holder den tom :)
     */
    @SuppressWarnings("unused")
    public User(){}

    public User(String name){
        setName(name);
        groups = new LinkedList<>();
    }

    public void addToGroup(Group g){
        groups.add(g);
    }

    public final List<Group> getGroups(){
        return groups;
    }
}
