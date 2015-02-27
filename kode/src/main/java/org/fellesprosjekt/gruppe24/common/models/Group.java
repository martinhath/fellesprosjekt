package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class Group extends Entity{

    private List<Entity> members;


    @SuppressWarnings("unused")
    public Group(){}

    public Group(int id, String name) {
        super(id, name);
        members = new LinkedList<>();
    }

    public void addMember(Entity e){
        if (members.contains(e))
            return;
        members.add(e);
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
