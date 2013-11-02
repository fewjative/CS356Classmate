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

public class FriendsListTab extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.tab_friends_list, null);

		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);

		RequestParams params = new RequestParams();
		params.put("email", userName);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getfriends.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Friend> friends = new ArrayList<Friend>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							friends.add(new Friend(jObj.getInt("friend_id"), jObj.getString("femail")));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				FriendListAdapter adapter = new FriendListAdapter(getActivity(), friends);
				ListView lvFriendsList = (ListView)root.findViewById(R.id.lvFriendsList);
				lvFriendsList.setAdapter(adapter);
			}
		});

		return root;
	}
}