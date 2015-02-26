package org.fellesprosjekt.gruppe24.common.models;

public class Room {
	
	private int capacity;
	private String name;
	private String location;
	
	public Room(String name, String location, int capacity) {
		this.name = name;
		this.location = location;
		this.capacity = capacity;
	}
	
	public Room() {}

	public int getCapacity() {
		return capacity;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLocation() {
		return location;
	}
	
	

}
