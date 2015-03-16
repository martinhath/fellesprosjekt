package org.fellesprosjekt.gruppe24.common.models.net;


import java.util.Map;

public class InvitationRequest extends Request {

	public enum Answer {YES, NO, MAYBE};
	
	public Answer ans;

	// Skal være på formen {<'userId': user.getId()>, <'meetingId': meeting.getId()>}
	public Map<String, Integer> ids;

	public InvitationRequest() {}
	
	public InvitationRequest(Type type, Map<String, Integer> ids, Answer ans) {
		super(type, ids);
		this.ans = ans;
	}
}
