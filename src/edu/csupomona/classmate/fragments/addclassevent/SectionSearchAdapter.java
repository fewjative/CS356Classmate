package edu.csupomona.classmate.fragments.addclassevent;

import android.app.Activity;
import static android.app.Activity.RESULT_OK;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.csupomona.classmate.AddClassActivity;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.SectionDetailsActivity;
import edu.csupomona.classmate.abstractions.Section;

import java.util.List;

public class SectionSearchAdapter extends ArrayAdapter<Section> implements View.OnClickListener, Constants {
	public SectionSearchAdapter(Context context, List<Section> schedule) {
		super(context, 0, schedule);
	}

	private static class ViewHolder {
		final TextView tvClassNumber;
		final TextView tvClassTitle;
		final TextView tvClassDays;
		final TextView tvClassTime;
		final TextView tvClassLecturer;
		final Button btnAddClass;
		final Button btnViewSectionDetails;

		ViewHolder(TextView tvClassNumber, TextView tvClassTitle, TextView tvClassDays, TextView tvClassTime, TextView tvClassLecturer, Button btnAddClass, Button btnViewSectionDetails) {
			this.tvClassNumber = tvClassNumber;
			this.tvClassTitle = tvClassTitle;
			this.tvClassDays = tvClassDays;
			this.tvClassTime = tvClassTime;
			this.tvClassLecturer = tvClassLecturer;
			this.btnAddClass = btnAddClass;
			this.btnViewSectionDetails = btnViewSectionDetails;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Section s = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.addclass_section_search_adapter_layout, null);

			TextView tvClassNumber = (TextView)view.findViewById(R.id.tvClassNumber);
			TextView tvClassTitle = (TextView)view.findViewById(R.id.tvClassTitle);
			TextView tvClassDays = (TextView)view.findViewById(R.id.tvClassDays);
			TextView tvClassTime = (TextView)view.findViewById(R.id.tvClassTime);
			TextView tvClassLecturer = (TextView)view.findViewById(R.id.tvClassLecturer);
			Button btnAddClass = (Button)view.findViewById(R.id.btnRemoveClass);
			Button btnViewSectionDetails = (Button)view.findViewById(R.id.btnViewSectionDetails);
			view.setTag(new ViewHolder(tvClassNumber, tvClassTitle, tvClassDays, tvClassTime, tvClassLecturer, btnAddClass, btnViewSectionDetails));

			tvClassTitle.setSelected(true);

			btnAddClass.setTag(s);
			btnAddClass.setOnClickListener(this);
			btnAddClass.setText("Add Class");

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
				holder.tvClassNumber.setText(String.format("%s %s.%s", s.getMajorShort(), s.getClassNum(), s.getSection()));
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

		return view;
	}

	public void onClick(View v) {
		Section s = (Section)v.getTag();
		switch (v.getId()) {
			case R.id.btnRemoveClass:
				addClass(s);
				break;
			case R.id.btnViewSectionDetails:
				viewSectionDetails(s);
				break;
		}
	}

	private void viewSectionDetails(final Section s) {
		Intent i = new Intent(((Activity)getContext()), SectionDetailsActivity.class);
		i.putExtra(INTENT_KEY_SECTION, s);
		i.putExtra(INTENT_KEY_USERID, ((Activity)getContext()).getIntent().getLongExtra(INTENT_KEY_USERID, NO_USER));
		((Activity)getContext()).startActivity(i);
	}

	public void addClass(final Section s) {
		long id = ((AddClassActivity)getContext()).getIntent().getLongExtra(INTENT_KEY_USERID, NO_USER);

		RequestParams params = new RequestParams();
		params.put("user_id", Long.toString(id));
		params.put("class_id", Integer.toString(s.getClassID()));

		AsyncHttpClient client = new AsyncHttpClient();
		//CHANGED TO ADDCLASS2
		client.get("http://lol-fc.com/classmate/addclass2.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				((AddClassActivity)getContext()).setResult(RESULT_OK);
				((AddClassActivity)getContext()).finish();
			}
		});
	}
}