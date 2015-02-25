package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class Group extends Entity{
    private List<Entity> members;

    @SuppressWarnings("unused")
    public Group(){}

    public Group(String name) {
        super(name);
        members = new LinkedList<>();
    }

    public void addMember(Entity e){
        if (members.contains(e))
            return;
        members.add(e);
        e.getGroups().add(this);
    }

    public List<Entity> getMembers(){
        return members;
    }

    public void printMembers(){
        for (Entity e : getMembers()) {
            if (e instanceof Group)
                ((Group) e).printMembers();
            else
                System.out.println(e.getName());
        }
    }
}
