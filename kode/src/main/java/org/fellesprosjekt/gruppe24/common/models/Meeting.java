package org.fellesprosjekt.gruppe24.common.models;

import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;

import java.time.LocalDateTime;
import java.util.List;

public class Meeting {



	private int id;


	private String name;
	private String description;
	private Room room;
	private LocalDateTime from;
	private LocalDateTime to;
	private List<User> participant;
	private User owner;
	private Group group;
	private String location;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	public Meeting(String name,
				   String description,
				   Room room,
				   LocalDateTime from,
				   LocalDateTime to,
				   String location,
				   List<User> participant,
				   User owner) {
		this.name = name;
		this.description = description;
		this.room = room;
		this.from = from;
		this.to = to;
		this.location = location;
		this.participant = participant;
		this.owner = owner;

		this.id = MeetingDatabaseHandler.getNextId();
	}

	public int getId() {
		return this.id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDateTime getFrom() {
		return from;
	}

	public void setFrom(LocalDateTime from) {
		this.from = from;
	}

	public LocalDateTime getTo() {
		return to;
	}

	public void setTo(LocalDateTime to) {
		this.to = to;
	}

	public List<User> getParticipant() {
		return participant;
	}

	public void setParticipant(List<User> participant) {
		this.participant = participant;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}



}
