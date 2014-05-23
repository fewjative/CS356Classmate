package edu.csupomona.classmate.abstractions;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.utils.AsyncGetAvatarTask;

public class User implements Parcelable {
	private final long ID;
	private final String USERNAME;
	private final String EMAIL;
	private Bitmap avatar;

	public User(long id, String username, String email) {
		this.ID = id;
		this.USERNAME = username;
		this.EMAIL = email;
		this.avatar = null;
	}

	private User(Parcel source) {
		this.ID = source.readLong();
		this.USERNAME = source.readString();
		this.EMAIL = source.readString();
		this.avatar = source.readParcelable(Drawable.class.getClassLoader());
	}

	public long getID() {
		return ID;
	}

	public String getUsername() {
		return USERNAME;
	}

	public String getEmail() {
		return EMAIL;
	}

	public void loadAvatar(final ImageView iv) {
		for (String ext : Constants.AVATAR_EXTENSIONS) {
			new AsyncGetAvatarTask(iv).execute(Constants.PHP_ADDRESS_UPLOADS + Long.toString(ID) + "." + ext);
		}
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(ID);
		dest.writeString(USERNAME);
		dest.writeString(EMAIL);
		dest.writeParcelable(avatar, PARCELABLE_WRITE_RETURN_VALUE);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel source) {
			return new User(source);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};
}
