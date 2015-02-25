package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class User extends Entity {

    // The users calendar
    private Calendar calendar;

    /**
     * Vi må ha en konstruktør som ikke tar inn noen argumenter
     * pga. Kryonet. Denne blir kjørt, så vi holder den tom :)
     */
    @SuppressWarnings("unused")
    public User(){}

    public User(String name){
        super(name);
    }

    /**
     * Denne bør bare bli kalt av Group.addMember(Entity);
     * @param g
     */
    public void addGroup(Group g){
        if (groups.contains(g))
            return;
        groups.add(g);
    }
}
