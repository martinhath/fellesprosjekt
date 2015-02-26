package org.fellesprosjekt.gruppe24.common.models;


public abstract class Entity {

    private String name;
    private Calendar calendar;


    public Entity(){}

    public Entity(String name){
        this();
        this.name = name;
        this.calendar = new Calendar();
    }

    public String getName() {
        return name;
    }
   
    public Calendar getCalendar() {
    	return calendar;
    }


}
