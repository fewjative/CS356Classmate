package edu.csupomona.classmate.abstractions;

public class ClassAddedEvent extends ActivityFeedItem {
	private final String CLASS_NAME;

	public ClassAddedEvent(User friend, String className) {
		super(friend);
		this.CLASS_NAME = className;
	}

	public String getClassName() {
		return CLASS_NAME;
	}
}
