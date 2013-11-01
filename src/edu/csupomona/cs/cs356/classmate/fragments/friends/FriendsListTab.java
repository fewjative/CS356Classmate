package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.LoginActivity;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.FriendListArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsListTab extends ListFragment implements View.OnClickListener {
	private List<Friend> friends;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		friends = new ArrayList<Friend>();

		String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);

		RequestParams params = new RequestParams();
		params.put("email", userName);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.lol-fc.com/classmate/getfriends.php", params, new AsyncHttpResponseHandler() {
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
							id = jObj.getInt("friend_id");
							userName = jObj.getString("femail");
							friends.add(new Friend(id, userName));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

					List<String> friendUsernames = new ArrayList<String>(friends.size());
					for (Friend f : friends) {
						friendUsernames.add(f.getUsername());
					}

					FriendListArrayAdapter adapter = new FriendListArrayAdapter(getActivity(), friendUsernames);
					setListAdapter(adapter);
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_list, null);
		//return root;

		View rootView = inflater.inflate(R.layout.fragment_list, container, false);
		View rowView = inflater.inflate(R.layout.tab_friends_list, container, false);

		Button btnRemove = (Button)rowView.findViewById(R.id.button1);
		btnRemove.setOnClickListener(this);

		Button btnViewSchedule = (Button)rowView.findViewById(R.id.button2);
		btnViewSchedule.setOnClickListener(this);

		return rootView;
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button1:
				myClickMethodRemove(v);
				break;
			case R.id.button2:
				myClickMethodView(v);
				break;
		}
	}

	public void myClickMethodRemove(View v) {
		int pos = getListView().getPositionForView((View)v.getParent());
		final Friend friend = friends.get(pos);

		AlertDialog d = new AlertDialog.Builder(getActivity()).create();
		d.setTitle(R.string.removeTitle);
		d.setMessage(getResources().getString(R.string.removeConfirmation));
		d.setIcon(android.R.drawable.ic_dialog_info);
		d.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "Clicked Yes", Toast.LENGTH_SHORT).show();

				String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
				String id = Integer.toString(friend.getID());

				RequestParams params = new RequestParams();
				params.put("email", userName);
				params.put("user_id", id);
				params.put("version", "1");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/removefriend.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						friends.remove(friend);

						List<String> friendUsernames = new ArrayList<String>(friends.size());
						for (Friend f : friends) {
							friendUsernames.add(f.getUsername());
						}

						FriendListArrayAdapter adapter = new FriendListArrayAdapter(getActivity(), friendUsernames);
						setListAdapter(adapter);
					}
				});
			}
		});

		d.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getActivity(), "Clicked No", Toast.LENGTH_SHORT).show();

				String userName = getActivity().getIntent().getStringExtra(LoginActivity.INTENT_KEY_USERNAME);
				String id = Integer.toString(friend.getID());

				RequestParams params = new RequestParams();
				params.put("email", userName);
				params.put("user_id", id);
				params.put("version", "2");

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/removefriend.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						friends.remove(friend);

						List<String> friendUsernames = new ArrayList<String>(friends.size());
						for (Friend f : friends) {
							friendUsernames.add(f.getUsername());
						}

						FriendListArrayAdapter adapter = new FriendListArrayAdapter(getActivity(), friendUsernames);
						setListAdapter(adapter);
					}

				});
			}
		});

		d.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//...
			}
		});

		d.show();

	}

	public void myClickMethodView(View v) {
		final int pos = getListView().getPositionForView((View)v.getParent());
		Toast.makeText(getActivity(), friends.get(pos).getUsername() + " selected", Toast.LENGTH_SHORT).show();
	}
}