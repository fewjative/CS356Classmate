package edu.csupomona.cs.cs356.classmate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleFragment extends Fragment {
	// TODO this doesn't do anything...
	private static final ScheduleFragment INSTANCE = new ScheduleFragment();

	public static ScheduleFragment newInstance() {
		return INSTANCE;
	}

	private LinearLayout llSchedule;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup root = (ViewGroup)inflater.inflate(R.layout.schedule_fragment, null);

		final int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);

		final LinearLayout llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("user_id", Integer.toString(id));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getusernumclasses.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				llProgressBar.setVisibility(View.GONE);

				int numClasses;
				try {
					numClasses = Integer.parseInt(response);
				} catch (NumberFormatException e) {
					numClasses = 0;
				}

				if (numClasses == 0) {
					LinearLayout llAddClass = (LinearLayout)root.findViewById(R.id.llAddClass);
					llAddClass.setVisibility(View.VISIBLE);

					ImageButton btnAddClass = (ImageButton)root.findViewById(R.id.btnAddClass);
					btnAddClass.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent i = new Intent(getActivity(), AddClassActivity.class);
							i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
							startActivityForResult(i, 1);
						}
					});
				} else {
					llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);
					llSchedule.setVisibility(View.VISIBLE);

					Button btnAddClass2 = (Button)root.findViewById(R.id.btnAddClass2);
					btnAddClass2.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent i = new Intent(getActivity(), AddClassActivity.class);
							i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
							startActivityForResult(i, 1);
						}
					});

					final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
					llProgressBarClasses.setVisibility(View.VISIBLE);

					RequestParams params = new RequestParams();
					params.put("full", "yes");
					params.put("user_id", Integer.toString(id));

					AsyncHttpClient client = new AsyncHttpClient();
					client.get("http://lol-fc.com/classmate/getuserclasses.php", params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							setupSchedule(response);
							llProgressBarClasses.setVisibility(View.GONE);
						}
					});
				}
			}
		});

		return root;
	}

	private void setupSchedule(String response) {
		List<Section> schedule = new ArrayList<Section>();
		if (1 < response.length()) {
			try {
				Section s;
				JSONObject jObj;
				JSONArray myjsonarray = new JSONArray(response);
				for (int i = 0; i < myjsonarray.length(); i++) {
					jObj = myjsonarray.getJSONObject(i);

					s = new Section();
					s.class_id = jObj.getInt("class_id");
					s.title = jObj.getString("title");
					s.time_start = jObj.getString("time_start");
					s.time_end = jObj.getString("time_end");
					s.weekdays = jObj.getString("weekdays");
					s.date_start = jObj.getString("date_start");
					s.date_end = jObj.getString("date_end");
					s.instructor = jObj.getString("instructor");
					s.building = jObj.getString("building");
					s.room = jObj.getString("room");
					s.section = jObj.getString("section");
					s.major_short = jObj.getString("major_short");
					s.major_long = jObj.getString("major_long");
					s.class_num = jObj.getString("class_num");
					s.term = jObj.getString("term");

					schedule.add(s);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		ScheduleAdapter adapter = new ScheduleAdapter(getActivity(), schedule);
		ListView lvSchedule = (ListView)llSchedule.findViewById(R.id.lvSchedule);
		lvSchedule.setAdapter(adapter);
	}
}
