package edu.csupomona.cs.cs356.classmate.abstractions;

public class Review {
	private final int review_id;
	private final String username;
	private final int class_id;
	private final String title;
	private final String review_text;
	private final int review_rating;
	private final String date;

	public Review(
		int review_id,
		String username,
		int class_id,
		String title,
		String review_text,
		int review_rating,
		String date
	) {
		this.review_id = review_id;
		this.username = username;
		this.class_id = class_id;
		this.title = title;
		this.review_text = review_text;
		this.review_rating = review_rating;
		this.date = date;
	}

	public int getReviewID() {
		return review_id;
	}

	public String getUsername() {
		return username;
	}

	public int getClassID() {
		return class_id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return review_text;
	}

	public int getRating() {
		return review_rating;
	}

	public String getDate() {
		return date;
	}
}
