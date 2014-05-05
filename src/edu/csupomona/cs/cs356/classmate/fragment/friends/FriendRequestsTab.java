package edu.csupomona.cs.cs356.classmate.fragment.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class FriendRequestsTab extends Fragment {
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View root = (ViewGroup)inflater.inflate(R.layout.friend_requests_tab_layout, null);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		User user = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, user.getEmail());

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_GETREQUESTS, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray jsona) {
				List<User> requests = new ArrayList<User>();

				try {
					JSONObject jObj;
					for (int i = 0; i < jsona.length(); i++) {
						jObj = jsona.getJSONObject(i);
						requests.add(new User(
							jObj.getInt(Constants.PHP_PARAM_USERID),
							jObj.getString(Constants.PHP_PARAM_NAME),
							jObj.getString(Constants.PHP_PARAM_EMAIL)
						));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				FriendRequestsListAdapter adapter = new FriendRequestsListAdapter(getActivity(), requests);
				ListView lvRequestList = (ListView)root.findViewById(R.id.lvRequestList);
				if (adapter.isEmpty()) {
					LinearLayout llEmptyList = (LinearLayout)root.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvRequestList.setAdapter(adapter);
					// TODO uncomment
					//((MainActivity)getActivity()).updateFriendRequestsNum();
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return root;
	}
}