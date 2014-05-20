package edu.csupomona.classmate.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import static edu.csupomona.classmate.Constants.NULL_USER;
import static edu.csupomona.classmate.Constants.PHP_PARAM_USERID;
import edu.csupomona.classmate.abstractions.Activity;

public class ActivityFeedFragment extends Fragment {
	private LinearLayout llProgressBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = inflater.inflate(R.layout.activity_feed_layout, container, false);
		
		llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);
		
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		int id = getActivity().getIntent().getIntExtra(INTENT_KEY_USER, NULL_USER);
		RequestParams params = new RequestParams();
		
		params.put(PHP_PARAM_USERID, Integer.toString(id));
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getactivityfeed.php", params, new AsyncHttpResponseHandler() {
			public void onSuccess(JSONArray response){
				List<Activity> activities = new ArrayList<Activity>();
				try {
					JSONObject jObj;
					
					for (int i = 0; i < response.length(); i++){
						jObj = response.getJSONObject(i);
						activities.add(new Activity(jObj.getString("username"), jObj.getString("friendUsername"), jObj.getString("className")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ActivityFeedAdapter adapter = new ActivityFeedAdapter(getActivity(), activities);
				ListView lvActivityList = (ListView)ROOT.findViewById(R.id.lvActivityList);
				if (adapter.isEmpty()){
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					
					
					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvActivityList.setAdapter(adapter);
				}
				llProgressBar.setVisibility(View.GONE);
				
			}
		});

		ViewPager vpContentPane = (ViewPager)ROOT.findViewById(R.id.vpContentPane);
		

		return ROOT;
	}
	
	
}