package edu.csupomona.classmate.abstractions;

public class ActivityFeedItem {
	public static final int FRIEND_ADDED_EVENT = 0;
	public static final int CLASS_ADDED_EVENT = 1;

	private final User FRIEND;

	public ActivityFeedItem(User friend) {
		this.FRIEND = friend;
	}

	public User getFriend() {
		return FRIEND;
	}
}
