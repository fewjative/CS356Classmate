package edu.csupomona.classmate.abstractions;

public class FriendAddedEvent extends ActivityFeedItem {
	private final User PERSON_ADDED;

	public FriendAddedEvent(User friend, User personAdded) {
		super(friend);
		this.PERSON_ADDED = personAdded;
	}

	public User getPersonAdded() {
		return PERSON_ADDED;
	}
}
