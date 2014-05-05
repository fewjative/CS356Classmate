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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.Constants;
import static edu.csupomona.cs.cs356.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.User;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsListTab extends Fragment {
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = (ViewGroup)inflater.inflate(R.layout.friends_list_tab_layout, null);

		llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETFRIENDS, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsona) {
				List<User> friends = new ArrayList<User>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						friends.add(new User(
							jObj.getInt(Constants.PHP_PARAM_FRIENDID),
							jObj.getString(Constants.PHP_PARAM_FRIENDUSERNAME),
							jObj.getString(Constants.PHP_PARAM_FRIENDEMAIL)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				FriendListAdapter adapter = new FriendListAdapter(getActivity(), USER, friends);
				ListView lvFriendsList = (ListView)ROOT.findViewById(R.id.lvFriendsList);
				if (adapter.isEmpty()) {
					TextView tvEmptyList = (TextView)ROOT.findViewById(R.id.tvEmptyList);
					tvEmptyList.setText(getResources().getString(R.string.friends_list_empty, getResources().getString(R.string.friends_add_friend)));

					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvFriendsList.setAdapter(adapter);
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return ROOT;
	}
}
