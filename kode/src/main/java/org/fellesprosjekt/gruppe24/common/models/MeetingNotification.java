package org.fellesprosjekt.gruppe24.common.models;

import java.time.LocalTime;

public class MeetingNotification extends Notification {

	private Meeting meeting;
	private boolean hide;
	private LocalTime alarmTime;
	
	public MeetingNotification(User user, String message, boolean read, boolean confirmed, Meeting meeting, boolean hide, LocalTime alarmTime) {
		super(user, message, read, confirmed);
		this.meeting = meeting;
		this.hide = hide;
		this.alarmTime = alarmTime;
	}
	
	public MeetingNotification(User user, String message, Meeting meeting) {
		this(user, message, false, false, meeting, false, null);
	}
	
	public Meeting getMeeting() {
		return meeting;
	}
	
	public boolean hide() {
		return hide;
	}
	
	public LocalTime getAlarmTime() {
		return alarmTime;
	}
	
	public void setHide(boolean h) {
		this.hide = h;
	}
	
	public void setAlarmTime(LocalTime at) {
		this.alarmTime = at;
	}
	
}
