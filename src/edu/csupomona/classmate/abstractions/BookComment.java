package edu.csupomona.classmate.abstractions;

import android.widget.ImageView;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.utils.AsyncGetImageTask;

public class BookComment {
	private final String username;
	private final String comment;
	private final long comment_id;
	private final long user_id;

	public BookComment(long comment_id,long user_id,String username,String comment) {
		this.comment_id = comment_id;
		this.username = username;
		this.comment = comment;
		this.user_id = user_id;//really the commenters id
	}

	public String getComment() {
		return comment;
	}
	
	public long getCommentID() {
		return comment_id;
	}
	
	public String getCommenter()
	{
		return username;
	}
	
	@Override
	public String toString() {
		return username + " " + comment;
	}
	
	public void loadAvatar(final ImageView iv) {
		for (String ext : Constants.AVATAR_EXTENSIONS) {
			new AsyncGetImageTask(iv).execute(Constants.PHP_ADDRESS_UPLOADS + Long.toString(user_id) + "." + ext);
		}
	}
}
