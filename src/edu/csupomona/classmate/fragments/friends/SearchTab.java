package edu.csupomona.classmate.fragments.friends;

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
import static edu.csupomona.classmate.Constants.INTENT_KEY_USER;
import edu.csupomona.classmate.R;
import edu.csupomona.classmate.abstractions.User;
import edu.csupomona.classmate.utils.TextWatcherAdapter;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchTab extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup ROOT = (ViewGroup)inflater.inflate(R.layout.friend_search_tab_layout, null);
		final User USER = getActivity().getIntent().getParcelableExtra(INTENT_KEY_USER);
		final LinearLayout llProgressBar = (LinearLayout)ROOT.findViewById(R.id.llProgressBar);
		final ListView lvSearchResults = (ListView)ROOT.findViewById(R.id.lvSearchResults);
		final EditText etFriendName = (EditText)ROOT.findViewById(R.id.etFriendName);
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvSearchResults.setAdapter(null);

				RequestParams params = new RequestParams();
				params.put(Constants.PHP_PARAM_SEARCH, e.toString());
				params.put(Constants.PHP_PARAM_USERID, Long.toString(USER.getID()));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get(Constants.PHP_BASE_ADDRESS + Constants.PHP_ADDRESS_SEARCHUSERS, params, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsona) {
						List<User> searchResults = new LinkedList<User>();

						try {
							JSONObject jObj;
							for (int i = 0; i < jsona.length(); i++) {
								jObj = jsona.getJSONObject(i);
								searchResults.add(new User(
									jObj.getInt(Constants.PHP_PARAM_USERID),
									jObj.getString(Constants.PHP_PARAM_NAME),
									jObj.getString(Constants.PHP_PARAM_EMAIL)
								));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						SearchAdapter adapter = new SearchAdapter(getActivity(), USER, searchResults, etFriendName.getWindowToken());
						lvSearchResults.setAdapter(adapter);
						llProgressBar.setVisibility(View.GONE);
					}
				});
			}
		});

		//etFriendName.requestFocus();
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