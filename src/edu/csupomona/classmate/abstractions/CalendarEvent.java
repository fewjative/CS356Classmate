package edu.csupomona.classmate.abstractions;

import android.os.Parcel;

public class CalendarEvent extends NewsArticle {
	public CalendarEvent(String date, String title, String desc, String imageURL, String articleURL) {
		super(date, title, desc, imageURL, articleURL);
	}

	private CalendarEvent(Parcel source) {
		super(source);
	}
}
