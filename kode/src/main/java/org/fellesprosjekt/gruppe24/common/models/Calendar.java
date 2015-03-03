package org.fellesprosjekt.gruppe24.common.models;

import java.util.List;

public class Calendar {
    private User owner;
    private List<Meeting> meetings;
    
    
    public Calendar() {}
    
    public Calendar(User owner) {
    	this.owner = owner;
    }
    
    public User getOwner() {
    	return owner;
    }
    
    public List<Meeting> getMeetings() {
    	return meetings;
    }

    public boolean addMeeting(Meeting meeting) {
        return this.meetings.add(meeting);
    }
}
