package edu.csupomona.cs.cs356.classmate.fragments;

import static android.app.Activity.RESULT_OK;
import android.content.Context;
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
import edu.csupomona.cs.cs356.classmate.R;
import java.util.List;

public class SectionSearchAdapter extends ArrayAdapter<Section> implements View.OnClickListener {
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

		ViewHolder(TextView tvClassNumber, TextView tvClassTitle, TextView tvClassDays, TextView tvClassTime, TextView tvClassLecturer, Button btnAddClass) {
			this.tvClassNumber = tvClassNumber;
			this.tvClassTitle = tvClassTitle;
			this.tvClassDays = tvClassDays;
			this.tvClassTime = tvClassTime;
			this.tvClassLecturer = tvClassLecturer;
			this.btnAddClass = btnAddClass;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Section s = getItem(position);
		ViewHolder holder = null;
		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_list_item, null);

			TextView tvClassNumber = (TextView)view.findViewById(R.id.tvClassNumber);
			TextView tvClassTitle = (TextView)view.findViewById(R.id.tvClassTitle);
			TextView tvClassDays = (TextView)view.findViewById(R.id.tvClassDays);
			TextView tvClassTime = (TextView)view.findViewById(R.id.tvClassTime);
			TextView tvClassLecturer = (TextView)view.findViewById(R.id.tvClassLecturer);
			Button btnAddClass = (Button)view.findViewById(R.id.btnRemoveClass);
			view.setTag(new ViewHolder(tvClassNumber, tvClassTitle, tvClassDays, tvClassTime, tvClassLecturer, btnAddClass));

			tvClassTitle.setSelected(true);

			btnAddClass.setTag(s);
			btnAddClass.setOnClickListener(this);
			btnAddClass.setText("Add Class");
		}

		Object tag = view.getTag();
		if (tag instanceof ViewHolder) {
			holder = (ViewHolder)tag;
		}

		if (s != null && holder != null) {
			if (holder.tvClassNumber != null) {
				holder.tvClassNumber.setText(String.format("%s %s.%02d", s.getMajorShort(), s.getClassNum(), Integer.parseInt(s.getSection())));
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
		}
	}

	public void addClass(final Section s) {
		int id = ((AddClassActivity)getContext()).getIntent().getIntExtra("userID", NULL_USER);

		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(id));
		params.put("class_id", Integer.toString(s.getClassID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://lol-fc.com/classmate/addclass.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				((AddClassActivity)getContext()).setResult(RESULT_OK);
				((AddClassActivity)getContext()).finish();
			}
		});
	}
}