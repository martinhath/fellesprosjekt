package org.fellesprosjekt.gruppe24.common.models;

public class Notification {
	
	private User user;
	private Meeting meeting;
	
	public Notification(User user, Meeting meeting) {
		this.user = user;
		this.meeting = meeting;
	}
	
	public Notification() {}
	
	public User getUser() {
		return user;
	}
	
	public Meeting getMeeting() {
		return meeting;
	}

}
