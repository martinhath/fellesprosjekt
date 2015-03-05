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
	private List<Entity> participants;
	private User owner;
	private Group group;
	private String location;

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

		this.id = MeetingDatabaseHandler.insertMeeting(this);
	}
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
				", description='" + description + '\'' +
				", room=" + room +
				", from=" + from +
				", to=" + to +
				", participants=" + participants +
				", owner=" + owner +
				", group=" + group +
				", location='" + location + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Meeting meeting = (Meeting) o;

		if (id != meeting.id) return false;
		if (description != null ? !description.equals(meeting.description) : meeting.description != null) return false;
		if (!from.equals(meeting.from)) return false;
		if (group != null ? !group.equals(meeting.group) : meeting.group != null) return false;
		if (location != null ? !location.equals(meeting.location) : meeting.location != null) return false;
		if (!name.equals(meeting.name)) return false;
		if (!owner.equals(meeting.owner)) return false;
		if (!participants.equals(meeting.participants)) return false;
		if (room != null ? !room.equals(meeting.room) : meeting.room != null) return false;
		if (!to.equals(meeting.to)) return false;

		return true;
	}
}
