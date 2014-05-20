package edu.csupomona.classmate.abstractions;

import edu.csupomona.classmate.R;

public class Activity {
	private final String username;
	private final String friendUsername;
	private final String className;

	public Activity(String username, String friendUsername, String className) {
		this.username = username;
		this.friendUsername = friendUsername;
		this.className = className;
	}

	public String getUsername() {
		return username;
	}
	
	public String getFriendUsername(){
		return friendUsername;
	}
	
	public String getClassName(){
		return className;
	}
}
