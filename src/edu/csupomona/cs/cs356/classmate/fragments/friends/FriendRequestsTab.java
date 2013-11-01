package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.FriendRequestsArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestsTab extends ListFragment implements View.OnClickListener {
	private List<Friend> friends;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		friends = new ArrayList<Friend>();

		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);

		RequestParams params = new RequestParams();
		params.put("email", userName);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getrequests.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				if (response.length() == 1) {
					//empty list
				} else {
					try {
						int id;
						String userName;

						JSONObject jObj;
						JSONArray myjsonarray = new JSONArray(response);
						for (int i = 0; i < myjsonarray.length(); i++) {
							jObj = myjsonarray.getJSONObject(i);
							id = jObj.getInt("user_id");
							userName = jObj.getString("email");
							friends.add(new Friend(id, userName));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					List<String> friendUsernames = new ArrayList<String>(friends.size());
					for (Friend f : friends) {
						friendUsernames.add(f.getUsername());
					}

					FriendRequestsArrayAdapter adapter = new FriendRequestsArrayAdapter(getActivity(), friendUsernames);
					setListAdapter(adapter);
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_requests, null);
		//return root;

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		View rowView  = inflater.inflate(R.layout.tab_friends_requests, container, false);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button1:
				myClickMethodAccept(v);
				break;
			case R.id.button2:
				myClickMethodDismiss(v);
				break;
		}
	}

	public void myClickMethodAccept(View v) {
		System.out.println("collin selected accept button");
		int pos = getListView().getPositionForView((View)v.getParent());
		final Friend friend = friends.get(pos);

		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
		String id = Integer.toString(friend.getID());

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("user_id", id);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/acceptfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				friends.remove(friend);

				List<String> friendUsernames = new ArrayList<String>(friends.size());
				for (Friend f : friends) {
					friendUsernames.add(f.getUsername());
				}

				FriendRequestsArrayAdapter adapter = new FriendRequestsArrayAdapter(getActivity().getApplicationContext(), friendUsernames);
				setListAdapter(adapter);
			}

		});
	}

	public void myClickMethodDismiss(View v) {
		int pos = getListView().getPositionForView((View)v.getParent());
		final Friend friend = friends.get(pos);

		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
		String id = Integer.toString(friend.getID());

		RequestParams params = new RequestParams();
		params.put("email", userName);
		params.put("user_id", id);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/dismissfriend.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				friends.remove(friend);

				List<String> friendUsernames = new ArrayList<String>(friends.size());
				for (Friend f : friends) {
					friendUsernames.add(f.getUsername());
				}

				FriendRequestsArrayAdapter adapter = new FriendRequestsArrayAdapter(getActivity(), friendUsernames);
				setListAdapter(adapter);
			}

		});

	}
}