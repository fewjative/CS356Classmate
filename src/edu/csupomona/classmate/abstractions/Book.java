package edu.csupomona.classmate.abstractions;

import android.widget.ImageView;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.utils.AsyncReadImageTask;

public class Book {
	private final long booklist_id;
	private final long user_id;
	private final String title;
	private final String classname;
	private final String condition;
	private final String price;

	public Book(long booklist_id,long user_id,String title,String classname,String condition,String price) {
		this.booklist_id = booklist_id;
		this.user_id = user_id;
		this.title = title;
		this.classname = classname;
		this.condition = condition;
		this.price = price;
	}

	public String getTitle() {
		return title;
	}
	
	public String getClassname() {
		return classname;
	}
	
	public long getID()
	{
		return booklist_id;
	}
	
	public long getSellerID()
	{
		return user_id;
	}
	
	public String getCondition() {
		return condition;
	}
	
	public String getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "Book toString";
	}
	
	public void loadAvatar(final ImageView iv) {
		for (String ext : Constants.AVATAR_EXTENSIONS) {
			new AsyncReadImageTask(iv).execute(Constants.PHP_ADDRESS_UPLOADS + Long.toString(user_id) + "." + ext);
		}
	}
}
