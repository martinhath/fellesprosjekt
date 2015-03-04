package org.fellesprosjekt.gruppe24.common.models;

public abstract class Notification {
	
	private User user;
	private String message;
	private boolean read;
	private boolean confirmed;
	
	public Notification(User user, String message, boolean read, boolean confirmed) {
		this.user = user;
		this.message = message;
		this.read = read;
		this.confirmed = confirmed;
	}
	
	public Notification() {}
	
	public User getUser() {
		return user;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isRead() {
		return read;
	}
	
	public boolean isConfirmed() {
		return confirmed; 
	}
	
	public void setMessage(String s) {
		this.message = s;
	}
	
	public void setRead(boolean r) {
		this.read = r;
	}
	
	public void setConfirmed(boolean c) {
		this.confirmed = c;
	}

}
