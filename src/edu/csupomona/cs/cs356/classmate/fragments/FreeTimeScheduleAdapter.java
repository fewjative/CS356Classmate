package edu.csupomona.cs.cs356.classmate.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import edu.csupomona.cs.cs356.classmate.abstractions.TimeSlot;
import static edu.csupomona.cs.cs356.classmate.fragments.ScheduleFragment.CODE_VIEW_SECTION;
import java.util.List;

public class FreeTimeScheduleAdapter extends ArrayAdapter<TimeSlot> {
	private boolean outside_source = false;
	
	public FreeTimeScheduleAdapter(Context context, List<TimeSlot> freetime) {
		super(context, 0, freetime);
	}
	


	private static class ViewHolder {
		
		final TextView tvFreeTime;
	

		ViewHolder(TextView tvFreeTime) {
		
			this.tvFreeTime = tvFreeTime;
			
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TimeSlot ts = getItem(position);
		ViewHolder holder = null;
		View view = convertView;
		Button btnRemoveClass = null;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.freetime_schedule_list_item, null);

			TextView tvFreeTime = (TextView)view.findViewById(R.id.tvFreeTime);
			
			view.setTag(new ViewHolder(tvFreeTime));

		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (ts != null && holder != null) {
			if (holder.tvFreeTime != null) {
				holder.tvFreeTime.setText("Time: " + ts.getFullTime());
			}

		}
		
	
		return view;
	}

	/*
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
	}*/
}