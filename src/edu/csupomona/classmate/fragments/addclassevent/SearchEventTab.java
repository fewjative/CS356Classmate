package edu.csupomona.classmate.fragments.addclassevent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.classmate.Constants;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.fragments.friends.FriendRequestsAdapter;
import edu.csupomona.classmate.utils.TextWatcherAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchEventTab extends Fragment implements Constants{
	private EditText etFriendName;
	private ListView lvQueryResults;
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup ROOT = (ViewGroup)inflater.inflate(R.layout.addclassevent_search_event_fragment_tab_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);

		llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		lvQueryResults = (ListView)ROOT.findViewById(R.id.lvQueryResults);

		etFriendName = (EditText)ROOT.findViewById(R.id.etFriendName);
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvQueryResults.setAdapter(null);

				RequestParams params = new RequestParams();
				params.put(Constants.PHP_PARAM_SEARCH, e.toString());
				params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_SEARCHEVENTS, params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsona) {
						List<User> search_results = new ArrayList<User>();

						try {
							JSONObject jObj;
							for (int i = 0; i < jsona.length(); i++) {
								jObj = jsona.getJSONObject(i);
								search_results.add(new User(
									jObj.getLong(Constants.PHP_PARAM_USERID),
									jObj.getString(Constants.PHP_PARAM_NAME),
									jObj.getString(Constants.PHP_PARAM_EMAIL)
								));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						FriendRequestsAdapter adapter = new FriendRequestsAdapter(getActivity(), USER, search_results);
						lvQueryResults.setAdapter(adapter);
						llProgressBar.setVisibility(View.GONE);
					}
				});
			}
		});

		etFriendName.requestFocus();
		etFriendName.setFocusableInTouchMode(true);
		etFriendName.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				etFriendName.requestFocusFromTouch();
				return false;
			}

		});

		return ROOT;
	}
}