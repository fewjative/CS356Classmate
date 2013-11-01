package edu.csupomona.cs.cs356.classmate.fragments.friends;

public class Friend {
	private final int id;
	private final String username;

	public Friend(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public int getID() {
		return id;
	}

	public String getUsername() {
		return username;
	}
}
