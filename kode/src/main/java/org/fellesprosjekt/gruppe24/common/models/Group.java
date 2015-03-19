package org.fellesprosjekt.gruppe24.common.models;

import java.util.LinkedList;
import java.util.List;

public class Group extends Entity{

    private List<User> members;
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

    public void addMember(User e){
        if (members.contains(e))
            return;
        members.add(e);
    }
    
    public void setMembers(List<User> l) {
    	members = l;
    }

    public List<User> getMembers(){
        return members;
    }

    public int getOwnerId() {
    	return ownerId;
    }
    
    public void printMembers() {
    	String s = "";
    	for(Entity e : members) {
    		s += e.getName() + ", ";
    	}
    	System.out.println(s);
    }
    
}
