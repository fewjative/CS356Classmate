package edu.csupomona.cs.cs356.classmate.abstractions;

import edu.csupomona.cs.cs356.classmate.R;

public class Friend {
	private final int id;
	private final String username;
	private final String emailAddress;
	private final int avatar;

	public Friend(int id, String username, String emailAddress) {
		this.id = id;
		this.username = username;
		this.emailAddress = emailAddress;
		this.avatar = R.drawable.ic_action_person;
	}

	public int getID() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return emailAddress;
	}

	public int getAvatar() {
		return avatar;
	}
}
