package org.fellesprosjekt.gruppe24.common.models;

import org.fellesprosjekt.gruppe24.database.RoomDatabaseHandler;

public class Room implements Comparable<Room> {

	private int id;
	private int capacity;
	private String name;
	private boolean accessible;

	/**
	 * Creates an object where the ID already exists in the database
	 * Should only be called from the generate function in a database handler
	 * @param id
	 * @param name
	 * @param capacity
	 * @param accessible
	 */
	public Room(int id, String name, int capacity, boolean accessible) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
		this.accessible = accessible;
	}

	/**
	 * Creates an object without an ID which should only be used to create a database entry
	 * @param name
	 * @param capacity
	 * @param accessible
	 */
	public Room(String name, int capacity, boolean accessible) {
		this.name = name;
		this.capacity = capacity;
		this.accessible = accessible;
	}

	public Room() {}

	public int getId() {
		return id;
	}

	public int getCapacity() {
		return capacity;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isAccessible() {
		return accessible;
	}

	public boolean equals(Room other) {
		return (
				this.getId() == other.getId() &&
				this.getName().equals(other.getName()) &&
				this.accessible == other.accessible &&
				this.getCapacity() == other.getCapacity()
		);
	}

	@Override
	public String toString() {
		return "Room{" +
				"id=" + id +
				", capacity=" + capacity +
				", name='" + name + '\'' +
				", accessible=" + accessible +
				'}';
	}

	@Override
	public int compareTo(Room other) {
		return this.getCapacity() - other.getCapacity();
	}
}
