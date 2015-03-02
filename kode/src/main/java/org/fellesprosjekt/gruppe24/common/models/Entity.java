package org.fellesprosjekt.gruppe24.common.models;


public abstract class Entity {

    private String name;
    private Calendar calendar;
    private int Id;


    public Entity(){}

    public Entity(int Id, String name){
        this();
        this.Id = Id;
        this.name = name;
        this.calendar = new Calendar();
    }
    
    public Entity(int Id) {
    	this.Id = Id;
    }

    public String getName() {
        return name;
    }
   
    public Calendar getCalendar() {
    	return calendar;
    }

    public int getId() {
    	return Id;
    }

}
