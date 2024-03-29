package org.fellesprosjekt.gruppe24.common.models;

import org.fellesprosjekt.gruppe24.database.MeetingDatabaseHandler;
import java.time.LocalDateTime;
import java.util.List;

public class Meeting implements Comparable<Meeting> {



	private int id;


	private String name;
	private String description;
	private Room room;
	private LocalDateTime from;
	private LocalDateTime to;
	private List<Entity> participants;
	private User owner;
	private Group group;
	private String location;

	public Meeting() {}

	/**
	 * Creates a Meeting without an ID. This object is only used as an intermediary before inserting it into the
	 * database where you will get a full-fletched Meeting object with its own unique ID.
	 * @param name
	 * @param description
	 * @param room
	 * @param from
	 * @param to
	 * @param location
	 * @param participants
	 * @param owner
	 */
	public Meeting(String name,
				   String description,
				   Room room,
				   LocalDateTime from,
				   LocalDateTime to,
				   String location,
				   List<Entity> participants,
				   User owner) {
		this.name = name;
		this.description = description;
		this.room = room;
		this.from = from;
		this.to = to;
		this.location = location;
		this.participants = participants;
		this.owner = owner;
	}

	/**
	 * To create a Meeting object with an ID. Will only be called from the database handler of Meetings.
	 */
	public Meeting(int id,
				   String name,
				   String description,
				   Room room,
				   LocalDateTime from,
				   LocalDateTime to,
				   String location,
				   List<Entity> participants,
				   User owner) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.room = room;
		this.from = from;
		this.to = to;
		this.location = location;
		this.participants = participants;
		this.owner = owner;
	}
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
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


	public List<Entity> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Entity> participants) {
		this.participants = participants;
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

	@Override
	public String toString() {
		return "Meeting{" +
				"id=" + id +
				", name='" + name + '\'' +
				", from=" + from +
				", to=" + to +
				", owner=" + owner +
				", group=" + group +
				'}';
	}

	@Override
	public int compareTo(Meeting o) {
		return this.getFrom().compareTo(o.getFrom());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Meeting)) return false;
		Meeting rhs = (Meeting) o;
		return rhs.getId() == this.getId();
	}
}
