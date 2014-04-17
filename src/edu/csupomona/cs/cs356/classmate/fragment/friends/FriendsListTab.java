package edu.csupomona.cs.cs356.classmate.fragment.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.Constants;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsListTab extends Fragment {
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.friends_list_tab_layout, null);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		String email = getActivity().getIntent().getStringExtra(Constants.INTENT_KEY_EMAIL);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, email);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETFRIENDS, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				List<Friend> friends = new ArrayList<Friend>();
				if (1 < response.length()) {
					try {
						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							friends.add(new Friend(
								jObj.getInt(Constants.PHP_PARAM_FRIENDID),
								jObj.getString(Constants.PHP_PARAM_FRIENDUSERNAME),
								jObj.getString(Constants.PHP_PARAM_FRIENDEMAIL)
							));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				FriendListAdapter adapter = new FriendListAdapter(getActivity(), friends);
				ListView lvFriendsList = (ListView)root.findViewById(R.id.lvFriendsList);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)root.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(getResources().getString(R.string.friends_list_empty, getResources().getString(R.string.friends_add_friend)));

					LinearLayout llEmptyList = (LinearLayout)root.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvFriendsList.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return root;
	}
}
