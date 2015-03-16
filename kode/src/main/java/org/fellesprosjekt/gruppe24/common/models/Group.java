package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class Group extends Entity{

    private List<Entity> members;
    private int ownerId;

    @SuppressWarnings("unused")
    public Group(){}

    public Group(int id, String name, int ownerId) {
        super(id, name);
        members = new LinkedList<>();
        this.ownerId = ownerId;
    }
    
    public Group(String name, int ownerId) {
    	super(name);
    	members = new LinkedList<>();
    	this.ownerId = ownerId;
    }

    public void addMember(Entity e){
        if (members.contains(e))
            return;
        members.add(e);
    }

    public List<Entity> getMembers(){
        return members;
    }

    public int getOwnerId() {
    	return ownerId;
    }
    
    public String toString() {
    	return String.format("[Group] name: %s id: %s ownerId: %s", getName(), getId(), getOwnerId());
    }
}
