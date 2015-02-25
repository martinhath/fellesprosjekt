package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public abstract class Entity {

    private String name;

    protected List<Group> groups;

    public Entity(){}

    public Entity(String name){
        this.name = name;
        groups = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void addGroup(Group g){
        groups.add(g);
    }
}
