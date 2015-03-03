package org.fellesprosjekt.gruppe24.common.models;


import org.fellesprosjekt.gruppe24.database.UserDatabaseHandler;

public abstract class Entity {

    private String name;
    private Calendar calendar;
    private int id;


    public Entity(){}

    public Entity(String name){
        this();
        this.name = name;
        this.calendar = new Calendar();
    }

    public Entity(int id, String name){
        this();
        this.id = id;
        this.name = name;
        this.calendar = new Calendar();
    }
    
    public Entity(int id) {
    	this.id = id;
    }

    public String getName() {
        return name;
    }
   
    public Calendar getCalendar() {
    	return calendar;
    }

    public int getId() {
    	return id;
    }

    /**
     * Used when creating new object without ID until it has been inserted in the database
     * @param id a correct database index
     */
    protected void setId(int id) {
        this.id = id;
    }

}
