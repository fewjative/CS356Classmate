package edu.csupomona.classmate.abstractions;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import edu.csupomona.classmate.utils.AsyncGetImageTask;

public class NewsArticle implements Parcelable {
	private final String DATE;
	private final String TITLE;
	private final String DESC;
	private final String IMAGEURL;
	private final String ARTICLEURL;

	public NewsArticle(String date, String title, String desc, String imageURL, String articleURL) {
		this.DATE = date;
		this.TITLE = title;
		this.DESC = desc;
		this.IMAGEURL = imageURL;
		this.ARTICLEURL = articleURL;
	}

	private NewsArticle(Parcel source) {
		this.DATE = source.readString();
		this.TITLE = source.readString();
		this.DESC = source.readString();
		this.IMAGEURL = source.readString();
		this.ARTICLEURL = source.readString();
	}

	public String getDate() {
		return DATE;
	}

	public String getTitle() {
		return TITLE;
	}

	public String getDesc() {
		return DESC;
	}

	public String getImageURL() {
		return IMAGEURL;
	}

	public String getArticleURL() {
		return ARTICLEURL;
	}

	public void loadImage(final ImageView iv) {
		new AsyncGetImageTask(iv).execute(IMAGEURL);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(DATE);
		dest.writeString(TITLE);
		dest.writeString(DESC);
		dest.writeString(IMAGEURL);
		dest.writeString(ARTICLEURL);
	}

	public static final Parcelable.Creator<NewsArticle> CREATOR = new Parcelable.Creator<NewsArticle>() {
		public NewsArticle createFromParcel(Parcel source) {
			return new NewsArticle(source);
		}

		public NewsArticle[] newArray(int size) {
			return new NewsArticle[size];
		}
	};
}
