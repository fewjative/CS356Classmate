package edu.csupomona.cs.cs356.classmate.fragments;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import static edu.csupomona.cs.cs356.classmate.LoginActivity.INTENT_KEY_USERID;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.SectionDetailsActivity;
import static edu.csupomona.cs.cs356.classmate.SectionDetailsActivity.INTENT_KEY_SECTION;
import edu.csupomona.cs.cs356.classmate.abstractions.Section;
import static edu.csupomona.cs.cs356.classmate.fragments.ScheduleFragment.CODE_VIEW_SECTION;
import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Section> implements View.OnClickListener {
	private boolean outside_source = false;
	
	public ScheduleAdapter(Context context, List<Section> schedule) {
		super(context, 0, schedule);
	}
	
	public ScheduleAdapter(Context context, List<Section> schedule, boolean outside_source)
	{
		super(context,0,schedule);
		this.outside_source = outside_source;
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

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, null);

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
			
			DisplayMetrics displaymetrics = new DisplayMetrics();
			((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int height = displaymetrics.heightPixels;
			int width = displaymetrics.widthPixels;
			
			int cellHeight = (height / 5);
			int squareDim = cellHeight - (cellHeight / 4);
			
			grid.setRowCount(4);
			grid.setPadding((width / 10), (cellHeight / 8), 0, 0);
			tvCellClassTime.setText("Time: " + s.getFullTime());
			
			// Make the textViews fancy here
			tvClassNumber.setTextSize(cellHeight / 15);
			tvClassTitle.setTextSize(cellHeight / 16);
			tvClassDays.setTextSize(cellHeight / 16);
			tvCellClassTime.setTextSize(cellHeight / 16);
			
			// Need to fix this still
			int txtPad = (int)(squareDim - (tvClassNumber.getTextSize() + (3 * tvClassTitle.getTextSize()))) / 3;
			System.out.println("******************************* " + squareDim);
			System.out.println("*************************** " + tvClassNumber.getTextSize());
			System.out.println("*********************** " + tvClassTitle.getTextSize());
			System.out.println("******************** " + txtPad);
			
//			tvClassNumber.setPadding(0, 0, 0, txtPad);
//			tvClassTitle.setPadding(0, 0, 0, txtPad);
//			tvClassDays.setPadding(0, 0, 0, txtPad);
			
			innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,cellHeight));
			
			
			Resources res = getContext().getResources();
			int id = R.drawable.shader;
			
			DrawSquare pcc = new DrawSquare (this.getContext(), null, res, id);
		    Bitmap result = Bitmap.createBitmap(squareDim, squareDim, Bitmap.Config.ARGB_8888);
		    Canvas canvas = new Canvas(result);
		    pcc.draw(canvas);
		    pcc.setLayoutParams(new LayoutParams(squareDim, squareDim));
		    
		    innerLayout.addView(pcc, 0);
			pcc.setX((float)(width / 18));
			pcc.setY((float)(cellHeight / 8));
			
			btnViewSectionDetails.setVisibility(View.VISIBLE);
			btnViewSectionDetails.setTag(s);
			btnViewSectionDetails.setOnClickListener(this);
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (s != null && holder != null) {
			if (holder.tvClassNumber != null) {
				if(Integer.parseInt(s.getSection()) < 10)
				{
					holder.tvClassNumber.setText(String.format("%s %s   0%s", s.getMajorShort(), s.getClassNum(), s.getSection()));
				}else
					holder.tvClassNumber.setText(String.format("%s %s   %s", s.getMajorShort(), s.getClassNum(), s.getSection()));
			}

			if (holder.tvClassTitle != null) {
				holder.tvClassTitle.setText(s.getTitle());
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
		
		if(outside_source && btnRemoveClass!=null)
			btnRemoveClass.setVisibility(View.GONE);

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
		i.putExtra(INTENT_KEY_USERID, ((FragmentActivity)getContext()).getIntent().getIntExtra(INTENT_KEY_USERID, NULL_USER));
		((FragmentActivity)getContext()).startActivityForResult(i, CODE_VIEW_SECTION);
	}

	private void removeClass(final Section s) {
		int id = ((FragmentActivity)getContext()).getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);

		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(id));
		params.put("class_id", Integer.toString(s.getClassID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://lol-fc.com/classmate/removeclass.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				remove(s);
			}
		});
	}
}