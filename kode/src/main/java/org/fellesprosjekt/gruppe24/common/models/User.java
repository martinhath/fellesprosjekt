package org.fellesprosjekt.gruppe24.common.models;

import java.util.List;

public class User {

    private String name;
    private Calendar calendar;

    private List<Group> groups;

    public User(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
