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
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.SectionDetailsActivity;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.User;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Section> implements View.OnClickListener, Constants {
	private final User USER;
	private final User VIEWER;


	public ScheduleAdapter(Context context, User user, List<Section> schedule) {
		super(context, 0, schedule);
		this.USER = user;
		this.VIEWER = null;
	}

	public ScheduleAdapter(Context context, User user, List<Section> schedule, User viewer) {
		super(context,0,schedule);
		this.USER = user;
		this.VIEWER = viewer;
	}

	private static class ViewHolder {
		final TextView tvClassNumber;
		final TextView tvClassTitle;
		final TextView tvClassDays;
		final TextView tvClassTime;
		final TextView tvClassLecturer;
		final Button btnRemoveClass;
		final Button btnViewSectionDetails;

		ViewHolder(TextView tvClassNumber, TextView tvClassTitle, TextView tvClassDays, TextView tvClassTime, TextView tvClassLecturer, Button btnRemoveClass, Button btnViewSectionDetails) {
			this.tvClassNumber = tvClassNumber;
			this.tvClassTitle = tvClassTitle;
			this.tvClassDays = tvClassDays;
			this.tvClassTime = tvClassTime;
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
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, null);

			LinearLayout parentLayout = (LinearLayout)view.findViewById(R.id.menu_row);
			LinearLayout innerLayout = (LinearLayout)view.findViewById(R.id.innerLayout);
			GridLayout grid = (GridLayout)view.findViewById(R.id.gridForText);
			TextView tvClassNumber = (TextView)view.findViewById(R.id.tvClassNumber);
			TextView tvClassTitle = (TextView)view.findViewById(R.id.tvClassTitle);
			TextView tvClassDays = (TextView)view.findViewById(R.id.tvClassDays);
			TextView tvClassTime = (TextView)view.findViewById(R.id.tvClassTime);
			TextView tvCellClassTime = (TextView)view.findViewById(R.id.tvCellClassTime);
			TextView tvClassLecturer = (TextView)view.findViewById(R.id.tvClassLecturer);
			btnRemoveClass = (Button)view.findViewById(R.id.btnRemoveClass);
			Button btnViewSectionDetails = (Button)view.findViewById(R.id.btnViewSectionDetails);
			view.setTag(new ViewHolder(tvClassNumber, tvClassTitle, tvClassDays, tvClassTime, tvClassLecturer, btnRemoveClass, btnViewSectionDetails));

			tvClassTitle.setSelected(true);

			btnRemoveClass.setTag(s);
			btnRemoveClass.setOnClickListener(this);

			btnViewSectionDetails.setVisibility(View.VISIBLE);
			btnViewSectionDetails.setTag(s);
			btnViewSectionDetails.setOnClickListener(this);
			
			int cellHeight = (width / 2);
			
			grid.setRowCount(4);
			grid.setPadding((width / 14), (cellHeight / 16), 0, 0);
			
			String temp = s.getFullTime();
			String[] part = temp.split(" - ");
			
			tvCellClassTime.setText(part[0]);
			
			tvClassNumber.setTextSize(cellHeight / 16);
			tvClassTitle.setTextSize(cellHeight / 20);
			tvClassDays.setTextSize(cellHeight / 20);
			tvCellClassTime.setTextSize(cellHeight / 20);
			
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

			if (holder.tvClassTitle != null) // Formatting needed
			{
				holder.tvClassTitle.setText(s.getTitle());
				String original = s.getTitle();
				if(original.length() > 13)
				{
					
				}
			}

			if (holder.tvClassDays != null) {
				holder.tvClassDays.setText(s.getWeekdays());
			}

			if (holder.tvClassTime != null) {
				holder.tvClassTime.setText("Time: " + s.getFullTime());
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

	public void onClick(View v) {
		Section s = (Section)v.getTag();
		switch (v.getId()) {
			case R.id.btnViewSectionDetails:
				viewSectionDetails(s);
				break;
			case R.id.btnRemoveClass:
				removeClass(s);
				break;
		}
	}

	private void viewSectionDetails(final Section s) {
		Intent i = new Intent(((FragmentActivity)getContext()), SectionDetailsActivity.class);
		i.putExtra(INTENT_KEY_SECTION, s);
		i.putExtra(INTENT_KEY_USER, USER);
		((FragmentActivity)getContext()).startActivityForResult(i, CODE_VIEWSECTION);
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