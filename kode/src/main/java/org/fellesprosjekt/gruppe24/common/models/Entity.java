package org.fellesprosjekt.gruppe24.common.models;


public abstract class Entity {

    private int id;
    private String name;
    private Calendar calendar;
    private int ID;


    public Entity(){}

    public Entity(int ID, String name){
        this();
        this.ID = ID;
        this.name = name;
        this.calendar = new Calendar();
    }
    
    public Entity(int ID) {
    	this.ID = ID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
   
    public Calendar getCalendar() {
    	return calendar;
    }

    public int getID() {
    	return ID;
    }

}
