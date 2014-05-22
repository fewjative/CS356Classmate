package edu.csupomona.classmate.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.abstractions.Activity;

public class ActivityFeedFragment extends Fragment {
	private LinearLayout llProgressBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.activity_feed_layout, container, false);
		
		llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);
		
		final User USER = getActivity().getIntent().getParcelableExtra(Constants.INTENT_KEY_USER);
		long id = USER.getID();
		RequestParams params = new RequestParams();
		
		params.put(Constants.PHP_PARAM_USERID, Long.toString(id));
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETACTIVITYFEED, params, new JsonHttpResponseHandler() {
			public void onSuccess(JSONArray response){
				List<Activity> activities = new ArrayList<Activity>();
				try {
					JSONObject jObj;
					
					for (int i = 0; i < response.length(); i++){
						jObj = response.getJSONObject(i);
						activities.add(new Activity(
								new User(
										jObj.getLong(Constants.PHP_PARAM_USERID),
										jObj.getString(Constants.PHP_PARAM_NAME),
										""
										),
								new User(
										jObj.getLong(Constants.PHP_PARAM_FRIENDID),
										jObj.getString("friendname"),
										""
										),
								jObj.getString("class_id")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ActivityFeedAdapter adapter = new ActivityFeedAdapter(getActivity(), activities);
				ListView lvActivityList = (ListView)ROOT.findViewById(R.id.lvActivityList);
				if (adapter.isEmpty()){
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(getResources().getString(R.string.activity_feed_empty));
					
					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvActivityList.setAdapter(adapter);
				}
				llProgressBar.setVisibility(View.GONE);
				
			}
		});
		return ROOT;
	}
	
	
}