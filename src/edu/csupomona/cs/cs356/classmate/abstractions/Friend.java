package edu.csupomona.cs.cs356.classmate.abstractions;

import edu.csupomona.cs.cs356.classmate.R;

public class Friend {
	private final long id;
	private final String username;
	private final String emailAddress;
	private final int avatar;

	public Friend(long id, String username, String emailAddress) {
		this.id = id;
		this.username = username;
		this.emailAddress = emailAddress;
		
		if(this.id==18)
		{
			this.avatar = R.drawable.ic_action_person_collin;
		}else if(this.id==19)
		{
			this.avatar = R.drawable.ic_action_person_robert;
		}else if(this.id==30)
		{
			this.avatar = R.drawable.ic_action_person_josh;
		}
		else
		{
			this.avatar = R.drawable.ic_action_person;
		}
	}

	public long getID() {
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
