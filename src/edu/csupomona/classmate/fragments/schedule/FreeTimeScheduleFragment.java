package edu.csupomona.classmate.fragments.schedule;

import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
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

import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.Section;
import edu.csupomona.classmate.abstractions.TimeSlot;
import edu.csupomona.classmate.abstractions.User;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FreeTimeScheduleFragment extends Fragment {
	public static final int CODE_ADD_CLASS = 0x000D;
	public static final int CODE_VIEW_SECTION = 0x1FC3;

	private ViewGroup root;
	private LinearLayout llBothFree;
	private LinearLayout llFriendFree;
	private LinearLayout llFreeTime;
	
	private boolean outside_source=false;
	private final long friend_id;
	private long user_id;
	
	public FreeTimeScheduleFragment(long friend_id)
	{
		this.friend_id = friend_id;
		user_id = 0;
	}
	
	public FreeTimeScheduleFragment()
	{
		friend_id=0;
		user_id = 0;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = (ViewGroup)inflater.inflate(R.layout.freetime_schedule_fragment, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		user_id = USER.getID();	

		final LinearLayout llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		RequestParams params = new RequestParams();
		params.put("user_id", Long.toString(user_id));
		params.put("friend_id", Long.toString(friend_id));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getfreetimetoday.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				llProgressBar.setVisibility(View.GONE);

				int resp;
				boolean free = false;
				try {
					resp = Integer.parseInt(response);
					free = true;
				} catch (NumberFormatException e) {
					resp = -1;
				}

				if (resp == 1) {//both are free
					
					
						llBothFree = (LinearLayout)root.findViewById(R.id.llBothFree);
						llBothFree.setVisibility(View.VISIBLE);
					
				} else if(resp==3)
				{
					llFriendFree = (LinearLayout)root.findViewById(R.id.llFriendFree);
					llFriendFree.setVisibility(View.VISIBLE);
					//set textview to the user name of friend?
				}
				else
				{
					llFreeTime = (LinearLayout)root.findViewById(R.id.llFreeTime);
					llFreeTime.setVisibility(View.VISIBLE);

					
					final LinearLayout llProgressBarClasses = (LinearLayout)llFreeTime.findViewById(R.id.llProgressBarClasses);
					llProgressBarClasses.setVisibility(View.VISIBLE);
					
					setupFreeTime(response);
					llProgressBarClasses.setVisibility(View.GONE);
					
				}
			}
		});

		return root;
	}

	private void setupFreeTime(String response) {
		List<TimeSlot> freetime = new ArrayList<TimeSlot>();
		if (1 < response.length()) {
			try {
				TimeSlot ts;
				JSONObject jObj;
				JSONArray myjsonarray = new JSONArray(response);
				for (int i = 0; i < myjsonarray.length(); i++) {
					jObj = myjsonarray.getJSONObject(i);
					ts = new TimeSlot(
					
						jObj.getString("time_start"),
						jObj.getString("time_end")
						
					);

					freetime.add(ts);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		FreeTimeTabScheduleAdapter adapter;
		
		adapter = new FreeTimeTabScheduleAdapter(getActivity(), freetime);

		//ListView lvFreeTime = null;
		ListView lvFreeTime = (ListView)llFreeTime.findViewById(R.id.lvFreeTime);
		lvFreeTime.setAdapter(adapter);
		
		/*lvFreeTime.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				LinearLayout llExtendedInfo = (LinearLayout)view.findViewById(R.id.llExtendedInfo);
				llExtendedInfo.setVisibility(llExtendedInfo.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			}
		});*/ //disable on click for now
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			/*case CODE_ADD_CLASS:
				//if (resultCode != RESULT_OK) {
				//	break;
				//}

				addClass();
				break;
			case CODE_VIEW_SECTION:
				viewSection();
				break;*/
		}
	}

	

	private void viewSection() {
		//...
	}
}
