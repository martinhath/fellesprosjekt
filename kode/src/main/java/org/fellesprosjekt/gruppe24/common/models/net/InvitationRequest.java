package org.fellesprosjekt.gruppe24.common.models.net;


public class InvitationRequest extends Request {

	public enum Answer {YES, NO, MAYBE};
	
	private Answer ans;
	
	public InvitationRequest(Type type, int id, Answer ans) {
		super(type, id);
		this.ans = ans;
	}

	public Answer getAns() {
		return ans;
	}

	public void setAns(Answer ans) {
		this.ans = ans;
	}
	
}
