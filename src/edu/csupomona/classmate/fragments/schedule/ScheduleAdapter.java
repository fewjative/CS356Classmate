package edu.csupomona.classmate.fragments.schedule;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.SectionDetailsActivity;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.friends.MyFriendsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleAdapter extends ArrayAdapter<Section>implements Constants {
	private final User USER;
	private final User VIEWER;
	private int friendC;


	public ScheduleAdapter(Context context, User user, List<Section> schedule) {
		super(context, 0, schedule);
		this.USER = user;
		this.VIEWER = null;
		this.friendC = 0;
	}

	public ScheduleAdapter(Context context, User user, List<Section> schedule, User viewer) {
		super(context,0,schedule);
		this.USER = user;
		this.VIEWER = viewer;
		this.friendC = 0;
	}

	private static class ViewHolder {
		final TextView tvClassNumber;
		final TextView tvClassDays;
		final TextView tvClassTime;
		final TextView tvFriends;
		final TextView tvClassLecturer;
		final Button btnRemoveClass;
		final Button btnViewSectionDetails;

		ViewHolder(TextView tvClassNumber, TextView tvClassDays, TextView tvClassTime, TextView tvFriends, TextView tvClassLecturer, Button btnRemoveClass, Button btnViewSectionDetails) {
			this.tvClassNumber = tvClassNumber;
			this.tvClassDays = tvClassDays;
			this.tvClassTime = tvClassTime;
			this.tvFriends = tvFriends;
			this.tvClassLecturer = tvClassLecturer;
			this.btnRemoveClass = btnRemoveClass;
			this.btnViewSectionDetails = btnViewSectionDetails;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Section s = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		Button btnRemoveClass = null;
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, null);

			LinearLayout innerLayout = (LinearLayout)view.findViewById(R.id.innerLayout);
			GridLayout grid = (GridLayout)view.findViewById(R.id.gridForText);
			TextView tvClassNumber = (TextView)view.findViewById(R.id.tvClassNumber);
			TextView tvClassDays = (TextView)view.findViewById(R.id.tvClassDays);
			TextView tvClassTime = (TextView)view.findViewById(R.id.tvClassTime);
			TextView tvFriends = (TextView)view.findViewById(R.id.tvFriends);
			TextView tvCellClassTime = (TextView)view.findViewById(R.id.tvCellClassTime);
			TextView tvClassLecturer = (TextView)view.findViewById(R.id.tvClassLecturer);
			btnRemoveClass = (Button)view.findViewById(R.id.btnRemoveClass);
			Button btnViewSectionDetails = (Button)view.findViewById(R.id.btnViewSectionDetails);
			view.setTag(new ViewHolder(tvClassNumber, tvClassDays, tvClassTime, tvFriends, tvClassLecturer, btnRemoveClass, btnViewSectionDetails));


//			btnRemoveClass.setTag(s);
//			btnRemoveClass.setOnClickListener(this);

//			btnViewSectionDetails.setVisibility(View.VISIBLE);
//			btnViewSectionDetails.setTag(s);
//			btnViewSectionDetails.setOnClickListener(this);
			
			int cellHeight = (width / 2);
			
			grid.setRowCount(4);
			grid.setPadding((width / 14), (cellHeight / 10), 0, 0);
			
			String temp = s.getFullTime();
			String[] part = temp.split(" - ");
			
			tvCellClassTime.setText(part[0]);
			
			tvClassNumber.setTextSize(cellHeight / 16);
//			tvClassTitle.setTextSize(cellHeight / 20);
			tvClassDays.setTextSize(cellHeight / 20);
			tvCellClassTime.setTextSize(cellHeight / 20);
			tvFriends.setTextSize(cellHeight / 20);
			
			innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, cellHeight));
			
//			int dim = (width / 2) - (width / 16);
			
			Bitmap container = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.grid_unit);
//			container = Bitmap.createScaledBitmap(container, dim, dim, true);
			BitmapDrawable background = new BitmapDrawable(container);
			innerLayout.setBackgroundDrawable(background);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (s != null && holder != null) {
			if (holder.tvClassNumber != null) {
				if(Integer.parseInt(s.getSection()) < 10)
				{
					holder.tvClassNumber.setText(String.format("%s %s  0%s", s.getMajorShort(), s.getClassNum(), s.getSection()));
				}else
					holder.tvClassNumber.setText(String.format("%s %s  %s", s.getMajorShort(), s.getClassNum(), s.getSection()));
			}

			if (holder.tvClassDays != null) {
				holder.tvClassDays.setText(s.getWeekdays());
			}

			if (holder.tvClassTime != null) {
				holder.tvClassTime.setText("Time: " + s.getFullTime());
			}
			
			if (holder.tvFriends != null) {
//				RequestParams params = new RequestParams();
//				params.put("class_id", Integer.toString(s.getClassID()));
//				params.put("user_id", Long.toString(USER.getID()));
//				AsyncHttpClient client = new AsyncHttpClient();
//				client.get("http://www.lol-fc.com/classmate/getfriendsinclass.php", params, new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(String response) {
//						if (1 < response.length()) {
//							try {
//								JSONArray myjsonarray = new JSONArray(response);
//								System.out.println("******************* friend count: " + response.length());
//								friendC = myjsonarray.length();
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
				Random r = new Random();
				int friendsCount = r.nextInt(6);
//				holder.tvFriends.setText("Time: " + s.getFullTime()); // Going to need to write code to fetch friends count per class
				holder.tvFriends.setText(friendsCount + " friends");
			}

			if (holder.tvClassLecturer != null) {
				holder.tvClassLecturer.setText("Lecturer: " + s.getInstructor());
			}
		}

		if (VIEWER != null && btnRemoveClass != null) {
			btnRemoveClass.setVisibility(View.GONE);
		}
		return view;
	}

	public void viewSectionDetails(int position) 
	{
		Section s = getItem(position);
		Intent i = new Intent(((FragmentActivity)getContext()), SectionDetailsActivity.class);
		i.putExtra(INTENT_KEY_SECTION, s);
		i.putExtra(INTENT_KEY_USER, USER);
		((Activity)getContext()).startActivityForResult(i, CODE_VIEWSECTION);
		
	}

	private void removeClass(final Section s) {
		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));
		params.put(Constants.PHP_PARAM_CLASS_ID, Integer.toString(s.getClassID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_REMOVECLASS2, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(s);
			}
		});
	}
}