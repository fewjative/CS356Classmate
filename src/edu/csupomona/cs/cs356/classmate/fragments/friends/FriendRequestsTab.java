package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestsTab extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.tab_friends_requests, null);

		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);

		RequestParams params = new RequestParams();
		params.put("email", userName);

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
							requests.add(new Friend(jObj.getInt("user_id"), jObj.getString("username"), jObj.getString("email")));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				FriendRequestsAdapter adapter = new FriendRequestsAdapter(getActivity(), requests);
				ListView lvRequestList = (ListView)root.findViewById(R.id.lvRequestList);
				lvRequestList.setAdapter(adapter);
			}
		});

		return root;
	}
}