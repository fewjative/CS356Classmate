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
		
		switch(this.id){
		case 18: this.avatar = R.drawable.ic_action_person_collin;
		break;
		case 19: this.avatar = R.drawable.ic_action_person_robert;
		break;
		case 30: this.avatar = R.drawable.ic_action_person_josh;
		break;
		default: this.avatar = R.drawable.ic_action_person;
		}
		
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
