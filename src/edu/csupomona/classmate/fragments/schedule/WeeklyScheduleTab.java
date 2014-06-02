package edu.csupomona.classmate.fragments.schedule;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.ScheduleItem;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")//takes care of problem with line 46(stupid irrelevant constructor issue that appeared even with no changes)
public class WeeklyScheduleTab extends Fragment implements Constants {
	private User user;
	private ViewGroup root;
	private EditText etScheduleName;
	private Button btnAddSchedule;
	private LinearLayout llAddClass;
	private LinearLayout llNoClass;
	private LinearLayout llSchedule;
	private LinearLayout llNoSchedule;
	private LinearLayout llNoFriendSchedule;

	private final User VIEWER;

	public WeeklyScheduleTab() {
		this.VIEWER = null;
	}

	public WeeklyScheduleTab(User viewer) {
		this.VIEWER = viewer;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = (ViewGroup)inflater.inflate(R.layout.schedule_weekly_tab_layout, null);
		user = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		final long id;
		if (VIEWER != null) {
			id = VIEWER.getID();
		} else {
			id = user.getID();
		}

		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) root.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;

		TextView tvToday = (TextView)root.findViewById(R.id.tvToday);
		tvToday.setTextSize(height / 52);

		SimpleDateFormat df = new SimpleDateFormat("EEE, MMM d, ''yy");
		String date = df.format(Calendar.getInstance().getTime());
		tvToday.setText(date);
		tvToday.setPadding((width / 19), 0, 0, 0);

		int lineHeight = (height / 160);
		int prog = (int)(width * 0.92537);
		LinearLayout llContainer = (LinearLayout)root.findViewById(R.id.llLineContainer);
		llContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, lineHeight));
		LinearLayout llLine = (LinearLayout)root.findViewById(R.id.llLine);
		llLine.setLayoutParams(new LinearLayout.LayoutParams(prog, LayoutParams.MATCH_PARENT));

		final LinearLayout llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		final RequestParams params = new RequestParams();
		params.put("user_id", Long.toString(id));

		final AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/2/getusernumschedules.php", params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				llProgressBar.setVisibility(View.GONE);

				int numSchedules;
                try {
               	 	numSchedules = Integer.parseInt(response);
                } catch (NumberFormatException e) {
               	 	numSchedules = 0;
                }

                if(numSchedules==0)
                {
                	if(VIEWER==null)
                	{
                		llNoSchedule = (LinearLayout)root.findViewById(R.id.llNoSchedule);
                		llNoSchedule.setVisibility(View.VISIBLE);

			            etScheduleName = (EditText)root.findViewById(R.id.etScheduleName);
			           	btnAddSchedule = (Button)root.findViewById(R.id.btnAddSchedule);
			           	btnAddSchedule.setEnabled(false);

			           	TextWatcherAdapter scheduleNameTextWatcher = new TextWatcherAdapter() {
	                     String s1;

	                     @Override
	                     public void afterTextChanged(Editable e) {
	                             s1 = etScheduleName.getText().toString();

	                             if (!s1.isEmpty()) {
	                           	  btnAddSchedule.setEnabled(true);
	                             } else {
	                           	  btnAddSchedule.setEnabled(false);
	                             }
	                     	}
			           	};

			           	etScheduleName.addTextChangedListener(scheduleNameTextWatcher);
                	}
                	else
                	{
                		llNoFriendSchedule = (LinearLayout)root.findViewById(R.id.llNoFriendSchedule);
                		llNoFriendSchedule.setVisibility(View.VISIBLE);
                	}
                }
                else
                {
                	client.get("http://www.lol-fc.com/classmate/2/getusernumclasses2.php", params, new AsyncHttpResponseHandler() {
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

        						llNoClass = (LinearLayout)root.findViewById(R.id.llNoClass);
        						llNoClass.setVisibility(View.VISIBLE);
            				}
            				else
            				{
            					llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);
            					llSchedule.setVisibility(View.VISIBLE);

            					final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
            					llProgressBarClasses.setVisibility(View.VISIBLE);

            					RequestParams params = new RequestParams();
            					params.put("full", "yes");
            					params.put("user_id", Long.toString(id));

            					AsyncHttpClient client = new AsyncHttpClient();
            					//CHANGED TO VERSION2
            					client.get("http://lol-fc.com/classmate/2/getuserscheduleitems.php", params, new AsyncHttpResponseHandler() {
            						@Override
            						public void onSuccess(String response) {
            							setupSchedule(response);
            							llProgressBarClasses.setVisibility(View.GONE);
            						}
            					});
            				}
            			}
            		});
                }
			}
		});
		return root;
	}

	private void setupSchedule(String response) {
		List<ScheduleItem> schedule = new ArrayList<ScheduleItem>();
		if (1 < response.length()) {
			try {
				ScheduleItem s;
				JSONObject jObj;
				JSONArray myjsonarray = new JSONArray(response);
				for (int i = 0; i < myjsonarray.length(); i++) {
					jObj = myjsonarray.getJSONObject(i);

					try{
						schedule.add(new ScheduleItem(
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
								jObj.getString("term"),
								jObj.getInt("friends_in_class"),
								0,
								"",
								"",
								"",
								""
							));
					}catch (JSONException e)
					{
						schedule.add(new ScheduleItem(0,jObj.getString("title"),jObj.getString("time_start"),jObj.getString("time_end"),jObj.getString("weekdays"),jObj.getString("date_start"),jObj.getString("date_end"),"","","","","","","","",jObj.getInt("friends_in_class"),jObj.getInt("event_id"),jObj.getString("description"),jObj.getString("fpublic"),jObj.getString("opublic"), jObj.getString("isprivate")));
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		for(int i=0;i<schedule.size();i++)
		{
			System.out.println("T: "+schedule.get(i).getFullTime());
		}

		final ScheduleAdapter adapter;

		if (VIEWER != null) {
			adapter = new ScheduleAdapter(getActivity(), user, schedule, VIEWER);
		} else {
			adapter = new ScheduleAdapter(getActivity(), user, schedule);
		}

		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) root.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;

		int pad = (width / 38);

		GridView gridSchedule = (GridView)llSchedule.findViewById(R.id.gridSchedule);
		gridSchedule.setPadding((pad / 2), pad, (pad / 2), 0);
		gridSchedule.setVerticalSpacing(pad);
		gridSchedule.setHorizontalSpacing(pad);
		gridSchedule.setAdapter(adapter);
		gridSchedule.setOnItemClickListener(new GridView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				adapter.viewSectionDetails(position);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CODE_ADDCLASS:
				//if (resultCode != RESULT_OK) {
				//	break;
				//}

				addClass();
				break;
			case CODE_VIEWSECTION:
				viewSection();
				break;
		}
	}

	private void addClass() {
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		if (llAddClass != null) {
			llAddClass.setVisibility(View.GONE);
		}


		if (llSchedule == null) {
			llSchedule = (LinearLayout)root.findViewById(R.id.llSchedule);
		}

		llSchedule.setVisibility(View.VISIBLE);

		final LinearLayout llProgressBarClasses = (LinearLayout)llSchedule.findViewById(R.id.llProgressBarClasses);
		llProgressBarClasses.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("full", "yes");
		params.put("user_id", Long.toString(USER.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		//CHANGED TO GETUSERCLASSES2
		client.get("http://lol-fc.com/classmate/2/getuserclasses2.php", params, new AsyncHttpResponseHandler() {
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
