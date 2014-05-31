package edu.csupomona.classmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.abstractions.Review;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.friends.MyFriendsAdapter;
import edu.csupomona.classmate.fragments.schedule.ReviewAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SectionDetailsActivity extends Activity implements Constants{
	public static final String INTENT_KEY_SECTION = "section";

	public static final int CODE_CREATE_REVIEW = 0xFCD1;

	private LinearLayout llProgressBar;

	private LinearLayout llProgressBarReviews;

	private Button btnCreateReview;

	private User user;
	private Section section;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section_details_activity_layout);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		user = getIntent().getParcelableExtra(INTENT_KEY_USER);
		section = getIntent().getParcelableExtra(INTENT_KEY_SECTION);
		final User USER = this.getIntent().getParcelableExtra(INTENT_KEY_USER);

		getActionBar().setTitle(section.toString());

		
		Button btnCreateReview = (Button)findViewById(R.id.btnCreateReview);
		btnCreateReview.setWidth(width / 2);
		
		TextView tvCourseTitle = (TextView)findViewById(R.id.tvCourseTitle);
		tvCourseTitle.setText(section.getTitle());
		tvCourseTitle.setTextSize(height / 40);

		TextView tvSectionTime = (TextView)findViewById(R.id.tvSectionTime);
		tvSectionTime.setText(String.format("%s  %s", section.getFullTime(), section.getWeekdays()));
		tvSectionTime.setTextSize(height / 65);

		TextView tvSectionInstructor = (TextView)findViewById(R.id.tvSectionInstructor);
		tvSectionInstructor.setText(section.getInstructor());
		tvSectionInstructor.setTextSize(height / 65);

		TextView tvSectionBuildingRoom = (TextView)findViewById(R.id.tvSectionBuildingRoom);
		tvSectionBuildingRoom.setText(String.format("Bldg %s Rm %s", section.getBuilding(), section.getRoom()));
		tvSectionBuildingRoom.setTextSize(height / 65);
		
		TextView tvDate = (TextView)findViewById(R.id.tvDate);
		tvDate.setText(String.format("%s to %s", section.getDateStart(), section.getDateEnd()));
		tvDate.setTextSize(height / 65);

		
		
		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("class_id", Integer.toString(section.getClassID()));
		params.put("user_id", Long.toString(user.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getfriendsinclass.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<User> friends = new ArrayList<User>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							friends.add(new User(
									jObj.getInt(Constants.PHP_PARAM_USERID),
									jObj.getString(Constants.PHP_PARAM_NAME),
									jObj.getString(Constants.PHP_PARAM_EMAIL)
							));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				MyFriendsAdapter adapter = new MyFriendsAdapter(SectionDetailsActivity.this, USER, friends);
				ListView lvFriendsList = (ListView)findViewById(R.id.lvFriendsList);
				if (adapter.isEmpty()) {
					LinearLayout llEmptyList = (LinearLayout)findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvFriendsList.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		llProgressBarReviews = (LinearLayout)findViewById(R.id.llProgressBar);
		llProgressBarReviews.setVisibility(View.VISIBLE);

		params = new RequestParams();
		params.put("class_id", Integer.toString(section.getClassID()));

		client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getreviews.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Review> reviews = new ArrayList<Review>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							reviews.add(new Review(
								jObj.getInt("review_id"),
								jObj.getString("username"),
								jObj.getInt("class_id"),
								jObj.getString("title"),
								jObj.getString("review_text"),
								jObj.getString("review_rating"),
								jObj.getString("date")
							));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				ReviewAdapter adapter = new ReviewAdapter(SectionDetailsActivity.this, reviews);
				ListView lvReviewList = (ListView)findViewById(R.id.lvReviewList);
				lvReviewList.setOnItemClickListener(new ListView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						TextView tvReview = (TextView)view.findViewById(R.id.tvReview);
						AlertDialog b = new AlertDialog.Builder(SectionDetailsActivity.this).create();
						b.setMessage(tvReview.getText());
						b.setCanceledOnTouchOutside(true);
						b.show();
					}
				});

				if (adapter.isEmpty()) {
					LinearLayout llEmptyListReviews = (LinearLayout)findViewById(R.id.llEmptyListReviews);
					llEmptyListReviews.setVisibility(View.VISIBLE);
				} else {
					lvReviewList.setAdapter(adapter);
				}

				llProgressBarReviews.setVisibility(View.GONE);
			}
		});

		btnCreateReview = (Button)findViewById(R.id.btnCreateReview);
		btnCreateReview.setVisibility(View.VISIBLE);
		btnCreateReview.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(SectionDetailsActivity.this, SectionReviewActivity.class);
				i.putExtra(INTENT_KEY_SECTION, section);
				i.putExtra(INTENT_KEY_USER, user.getID());
				startActivityForResult(i, CODE_CREATE_REVIEW);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_CREATE_REVIEW:
				if (resultCode != RESULT_OK) {
					break;
				}

				llProgressBarReviews.setVisibility(View.VISIBLE);

				RequestParams params = new RequestParams();
				params.put("class_id", Integer.toString(section.getClassID()));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/getreviews.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						List<Review> reviews = new ArrayList<Review>();
						if (1 < response.length()) {
							try {
								JSONObject jObj;
								JSONArray myjsonarray = new JSONArray(response);
								for (int i = 0; i < myjsonarray.length(); i++) {
									jObj = myjsonarray.getJSONObject(i);
									reviews.add(new Review(
										jObj.getInt("review_id"),
										jObj.getString("username"),
										jObj.getInt("class_id"),
										jObj.getString("title"),
										jObj.getString("review_text"),
										jObj.getString("review_rating"),
										jObj.getString("date")
									));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						ReviewAdapter adapter = new ReviewAdapter(SectionDetailsActivity.this, reviews);
						ListView lvReviewList = (ListView)findViewById(R.id.lvReviewList);
						lvReviewList.setOnItemClickListener(new ListView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								TextView tvReview = (TextView)view.findViewById(R.id.tvReview);
								AlertDialog b = new AlertDialog.Builder(SectionDetailsActivity.this).create();
								b.setMessage(tvReview.getText());
								b.setCanceledOnTouchOutside(true);
								b.show();
							}
						});

						if (adapter.isEmpty()) {
							LinearLayout llEmptyListReviews = (LinearLayout)findViewById(R.id.llEmptyListReviews);
							llEmptyListReviews.setVisibility(View.VISIBLE);
						} else {
							lvReviewList.setAdapter(adapter);
						}

						llProgressBarReviews.setVisibility(View.GONE);
					}
				});

				break;
		}
	}
}
