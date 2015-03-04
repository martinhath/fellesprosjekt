package org.fellesprosjekt.gruppe24.common.models;


public class InvitationResponse {
	
	
	
	private int id;
	public enum Answer {YES, NO, MAYBE};
	private Answer ans;
	
	
	public InvitationResponse(int id, Answer ans) {
		this.id = id;
		this.ans = ans;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Answer getAns() {
		return ans;
	}


	public void setAns(Answer ans) {
		this.ans = ans;
	}
	
	
}
