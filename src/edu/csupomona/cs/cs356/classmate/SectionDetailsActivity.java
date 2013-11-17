package edu.csupomona.cs.cs356.classmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import static edu.csupomona.cs.cs356.classmate.LoginActivity.INTENT_KEY_USERID;
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import edu.csupomona.cs.cs356.classmate.abstractions.Review;
import edu.csupomona.cs.cs356.classmate.abstractions.Section;
import edu.csupomona.cs.cs356.classmate.fragments.ReviewAdapter;
import edu.csupomona.cs.cs356.classmate.fragments.friends.FriendListAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SectionDetailsActivity extends Activity {
	public static final String INTENT_KEY_SECTION = "section";

	private LinearLayout llProgressBar;

	private LinearLayout llProgressBarReviews;

	private int id;
	private Section section;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.section_details_activity);

		id = getIntent().getIntExtra(INTENT_KEY_USERID, NULL_USER);
		section = getIntent().getParcelableExtra(INTENT_KEY_SECTION);

		getActionBar().setTitle(String.format("%s Class Details", section.toString()));

		TextView tvCourseNum = (TextView)findViewById(R.id.tvCourseNum);
		tvCourseNum.setText(section.toString());

		TextView tvCourseTitle = (TextView)findViewById(R.id.tvCourseTitle);
		tvCourseTitle.setText(section.getTitle());

		TextView tvSectionTime = (TextView)findViewById(R.id.tvSectionTime);
		tvSectionTime.setText(String.format("%s  %s", section.getFullTime(), section.getWeekdays()));

		TextView tvSectionDate = (TextView)findViewById(R.id.tvSectionDate);
		tvSectionDate.setText(String.format("%s to %s", section.getDateStart(), section.getDateEnd()));

		TextView tvSectionInstructor = (TextView)findViewById(R.id.tvSectionInstructor);
		tvSectionInstructor.setText(section.getInstructor());

		TextView tvSectionBuildingRoom = (TextView)findViewById(R.id.tvSectionBuildingRoom);
		tvSectionBuildingRoom.setText(String.format("%s %s", section.getBuilding(), section.getRoom()));

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("class_id", Integer.toString(section.getClassID()));
		params.put("user_id", Integer.toString(id));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getfriendsinclass.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Friend> friends = new ArrayList<Friend>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							friends.add(new Friend(
								jObj.getInt("user_id"),
								jObj.getString("username"),
								jObj.getString("email")
							));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				FriendListAdapter adapter = new FriendListAdapter(SectionDetailsActivity.this, friends);
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
								jObj.getInt("review_rating"),
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
	}
}
