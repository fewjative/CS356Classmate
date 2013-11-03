package edu.csupomona.cs.cs356.classmate.fragments.friends;

import edu.csupomona.cs.cs356.classmate.R;

public class Friend {
	final int id;
	final String username;
	final int avatar;

	public Friend(int id, String username) {
		this.id = id;
		this.username = username;
		this.avatar = R.drawable.ic_action_person_dark;
	}

	public int getID() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public int getAvatar() {
		return avatar;
	}
}
