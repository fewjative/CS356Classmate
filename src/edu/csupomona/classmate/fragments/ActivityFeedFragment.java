package edu.csupomona.classmate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.ActivityFeedItem;
import edu.csupomona.classmate.abstractions.ClassAddedEvent;
import edu.csupomona.classmate.abstractions.FriendAddedEvent;
import edu.csupomona.classmate.abstractions.User;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityFeedFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = (ViewGroup)inflater.inflate(R.layout.http_query_listview_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		final LinearLayout llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		final RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETACTIVITYFEED, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<ActivityFeedItem> activityFeedItems = new LinkedList<ActivityFeedItem>();

				try {
					JSONObject jObj;
					ActivityFeedItem item = null;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						switch (jObj.getInt(Constants.PHP_PARAM_ACTIVITYTYPE)) {
							case ActivityFeedItem.FRIEND_ADDED_EVENT:
								item = new FriendAddedEvent(
									new User(
										jObj.getLong(Constants.PHP_PARAM_USERID),
										jObj.getString(Constants.PHP_PARAM_NAME),
										null
									),
									new User(
										jObj.getInt(Constants.PHP_PARAM_FRIENDID),
										jObj.getString(Constants.PHP_PARAM_FRIENDUSERNAME),
										null
									)
								);

								break;
							case ActivityFeedItem.CLASS_ADDED_EVENT:
								item = new ClassAddedEvent(
									new User(
										jObj.getLong(Constants.PHP_PARAM_USERID),
										jObj.getString(Constants.PHP_PARAM_NAME),
										null
									),
									jObj.getString("classname")
								);

								break;
							default:
								// Invalid activity event item
						}

						activityFeedItems.add(item);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				ActivityFeedAdapter adapter = new ActivityFeedAdapter(getActivity(), activityFeedItems);
				ListView lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(R.string.activity_feed_empty);

					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvQueryResults.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return ROOT;
	}
}
