package edu.csupomona.classmate.fragments.friends;

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
import edu.csupomona.classmate.Constants;
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendRequestsTab extends Fragment {
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View ROOT = (ViewGroup)inflater.inflate(R.layout.friend_requests_tab_layout, null);

		llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		llProgressBar.setVisibility(View.VISIBLE);

		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		RequestParams params = new RequestParams();
		params.put(Constants.PHP_PARAM_EMAIL, USER.getEmail());

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

				FriendRequestsListAdapter adapter = new FriendRequestsListAdapter(getActivity(), USER, requests);
				ListView lvRequestList = (ListView)ROOT.findViewById(R.id.lvRequestList);
				if (adapter.isEmpty()) {
					LinearLayout llEmptyList = (LinearLayout)ROOT.findViewById(R.id.llEmptyList);
					llEmptyList.setVisibility(View.VISIBLE);
				} else {
					lvRequestList.setAdapter(adapter);
					// TODO uncomment
					//((MainActivity)getActivity()).updateFriendRequestsNum();
				}

				llProgressBar.setVisibility(View.GONE);
			}
		});

		return ROOT;
	}
}