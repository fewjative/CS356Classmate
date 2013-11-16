package edu.csupomona.cs.cs356.classmate.abstractions;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Group implements Parcelable {
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

	private Group(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Group createFromParcel(Parcel in) {
			return new Group(in);
		}

		public Group[] newArray(int size) {
			return new Group[size];
		}
	};
}
