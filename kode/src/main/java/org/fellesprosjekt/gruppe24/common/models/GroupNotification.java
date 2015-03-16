package org.fellesprosjekt.gruppe24.common.models;

public class GroupNotification extends Notification {
	
	private Group group;

	public GroupNotification() {}

	public GroupNotification(User user, String message, boolean read, boolean confirmed, Group group) {
		super(user, message, read, confirmed);
		this.group = group;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public String toString() {
		return String.format("[GroupNotification] userid: %s groupid: %s read: %s confirmed: %s", getUser().getId(),
				getGroup().getId(), isRead(), isConfirmed());
	}
}
