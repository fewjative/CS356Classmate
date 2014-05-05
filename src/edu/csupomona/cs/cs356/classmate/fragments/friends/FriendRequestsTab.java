package edu.csupomona.cs.cs356.classmate.fragments.friends;

import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.MainActivity;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestsTab extends Fragment {
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.tab_friends_requests, null);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		String email = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put("email", email);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getrequests.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Friend> requests = new ArrayList<Friend>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							requests.add(new Friend(jObj.getLong("user_id"), jObj.getString("username"), jObj.getString("email")));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				FriendRequestsAdapter adapter = new FriendRequestsAdapter(getActivity(), requests);
				ListView lvRequestList = (ListView)root.findViewById(R.id.lvRequestList);
				if (adapter.isEmpty()) {
					LinearLayout llEmptyList = (LinearLayout)root.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvRequestList.setAdapter(adapter);
					((MainActivity)getActivity()).updateFriendRequestsNum();
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return root;
	}
}