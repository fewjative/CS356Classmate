package edu.csupomona.classmate.abstractions;

public class Activity {
	private final String username;
	private final String friendUsername;
	private final String className;

	public Activity(User friend, User added, String className) {
		this.username = username;
		this.friendUsername = friendUsername;
		this.className = className;
	}

	public String getAdded() {
		return username;
	}
	
	public String getFriend(){
		return friendUsername;
	}
	
	public String getClassName(){
		return className;
	}
}
