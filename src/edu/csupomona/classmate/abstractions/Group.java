package edu.csupomona.classmate.abstractions;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Group implements Parcelable {
	private final long ID;
	private final String NAME;

	private List<User> users;

	public Group(long id, String name) {
		this(id, name, null);
	}

	public Group(long id, String name, List<User> users) {
		this.ID = id;
		this.NAME = name;
		this.users = users;
	}

	private Group(Parcel source) {
		this.ID = source.readLong();
		this.NAME = source.readString();
	}

	public long getID() {
		return ID;
	}

	public String getName() {
		return NAME;
	}

	@Override
	public String toString() {
		return getName();
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(NAME);
	}

	public static final Parcelable.Creator<Group> CREATOR = new Parcelable.Creator<Group>() {
		public Group createFromParcel(Parcel source) {
			return new Group(source);
		}

		public Group[] newArray(int size) {
			return new Group[size];
		}
	};
}
