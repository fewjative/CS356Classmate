package edu.csupomona.cs.cs356.classmate.abstractions;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {
	private final int id;
	private final String name;
	private List<Friend> people;

	public Group(int id, String name) {
		this(id, name, null);
	}

	public Group(int id, String name, List<Friend> people) {
		this.id = id;
		this.name = name;
		this.people = people;
	}

	public int getID() {
		return id;
	}

	public String getTitle() {
		return name;
	}

	public void setPeople(List<Friend> people) {
		this.people = people;
	}

	public List<Friend> getPeople() {
		return people;
	}

	@Override
	public String toString() {
		return name;
	}
}
