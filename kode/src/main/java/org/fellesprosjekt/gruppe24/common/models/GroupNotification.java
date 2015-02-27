package org.fellesprosjekt.gruppe24.common.models;

public class GroupNotification extends Notification {
	
	private Group group;

	public GroupNotification(User user, String message, boolean read, boolean confirmed, Group group) {
		super(user, message, read, confirmed);
		this.group = group;
	}
	
	public Group getGroup() {
		return group;
	}
	
}
