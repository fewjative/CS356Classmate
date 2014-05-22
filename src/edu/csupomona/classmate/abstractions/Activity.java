package edu.csupomona.classmate.abstractions;

public class Activity {
	private final User friend;
	private final User added;
	private final String className;

	public Activity(User friend, User added, String className) {
		this.friend = friend;
		this.added = added;
		this.className = className;
	}

	public User getAdded() {
		return added;
	}
	
	public User getFriend(){
		return friend;
	}
	
	public String getClassName(){
		return className;
	}
}
