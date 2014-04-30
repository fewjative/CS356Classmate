package edu.csupomona.cs.cs356.classmate.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import edu.csupomona.cs.cs356.classmate.abstractions.Section;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")//takes care of problem with line 46
public class ScheduleFragment extends Fragment {
	public static final int CODE_ADD_CLASS = 0x000D;
	public static final int CODE_VIEW_SECTION = 0x1FC3;

	private ViewGroup root;
	private LinearLayout llAddClass;
	private LinearLayout llNoClass;
	private LinearLayout llSchedule;
	
	private boolean outside_source=false;
	private final int friend_id;
	
	public ScheduleFragment()
	{
		friend_id = 0;
	}
	
	public ScheduleFragment(int user_id)
	{
		outside_source = true;
		friend_id = user_id;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = (ViewGroup)inflater.inflate(R.layout.schedule_fragment, null);

		final int id;
		if(outside_source)
		{
			id = friend_id;
		}
		else
			id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);

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
					
					if(outside_source)
					{
						llNoClass = (LinearLayout)root.findViewById(R.id.llNoClass);
						llNoClass.setVisibility(View.VISIBLE);
					}
					else
					{
						llAddClass = (LinearLayout)root.findViewById(R.id.llAddClass);
						llAddClass.setVisibility(View.VISIBLE);
	
						ImageButton btnAddClass = (ImageButton)root.findViewById(R.id.btnAddClass);
						btnAddClass.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								Intent i = new Intent(getActivity(), AddClassActivity.class);
								i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
								startActivityForResult(i, CODE_ADD_CLASS);
							}
						});
					}
				} else {
					llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);
					llSchedule.setVisibility(View.VISIBLE);

					Button btnAddClass2 = (Button)root.findViewById(R.id.btnAddClass2);
					
					if(outside_source)
					{
						btnAddClass2.setVisibility(View.GONE);
					}
					
					btnAddClass2.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent i = new Intent(getActivity(), AddClassActivity.class);
							i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
							startActivityForResult(i, CODE_ADD_CLASS);
						}
					});

					final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
					llProgressBarClasses.setVisibility(View.VISIBLE);

					RequestParams params = new RequestParams();
					params.put("full", "yes");
					params.put("user_id", Integer.toString(id));

					AsyncHttpClient client = new AsyncHttpClient();
					//CHANGED TO VERSION2
					client.get("http://lol-fc.com/classmate/getuserclasses2.php", params, new AsyncHttpResponseHandler() {
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
					s = new Section(
						jObj.getInt("class_id"),
						jObj.getString("title"),
						jObj.getString("time_start"),
						jObj.getString("time_end"),
						jObj.getString("weekdays"),
						jObj.getString("date_start"),
						jObj.getString("date_end"),
						jObj.getString("instructor"),
						jObj.getString("building"),
						jObj.getString("room"),
						jObj.getString("section"),
						jObj.getString("major_short"),
						jObj.getString("major_long"),
						jObj.getString("class_num"),
						jObj.getString("term")
					);

					schedule.add(s);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		ScheduleAdapter adapter;
		
		if(outside_source)
		{
			adapter = new ScheduleAdapter(getActivity(), schedule, outside_source);
		}
		else
			adapter = new ScheduleAdapter(getActivity(), schedule);

		
		ListView lvSchedule = (ListView)llSchedule.findViewById(R.id.lvSchedule);
		lvSchedule.setAdapter(adapter);
		lvSchedule.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LinearLayout llExtendedInfo = (LinearLayout)view.findViewById(R.id.llExtendedInfo);
				llExtendedInfo.setVisibility(llExtendedInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_ADD_CLASS:
				//if (resultCode != RESULT_OK) {
				//	break;
				//}

				addClass();
				break;
			case CODE_VIEW_SECTION:
				viewSection();
				break;
		}
	}

	private void addClass() {
		final int id = getActivity().getIntent().getIntExtra(LoginActivity.INTENT_KEY_USERID, NULL_USER);

		if (llAddClass != null) {
			llAddClass.setVisibility(View.GONE);
		}


		if (llSchedule == null) {
			llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);

			Button btnAddClass2 = (Button)root.findViewById(R.id.btnAddClass2);
			btnAddClass2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), AddClassActivity.class);
					i.putExtra(LoginActivity.INTENT_KEY_USERID, id);
					startActivityForResult(i, CODE_ADD_CLASS);
				}
			});
		}

		llSchedule.setVisibility(View.VISIBLE);

		final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
		llProgressBarClasses.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("full", "yes");
		params.put("user_id", Integer.toString(id));

		AsyncHttpClient client = new AsyncHttpClient();
		//CHANGED TO GETUSERCLASSES2
		client.get("http://lol-fc.com/classmate/getuserclasses2.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				setupSchedule(response);
				llProgressBarClasses.setVisibility(View.GONE);
			}
		});
	}

	private void viewSection() {
		//...
	}
}
