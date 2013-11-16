package edu.csupomona.cs.cs356.classmate.fragments.friends;

import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
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
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import static edu.csupomona.cs.cs356.classmate.Constants.NULL_USER;
import static edu.csupomona.cs.cs356.classmate.LoginActivity.INTENT_KEY_USERID;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFriendTab extends Fragment {
	private EditText etFriendName;
	private ListView lvSearchResults;
	private LinearLayout llProgressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.tab_friends_add, null);

		llProgressBar = (LinearLayout)root.findViewById(R.id.llProgressBar);
		lvSearchResults = (ListView)root.findViewById(R.id.lvSearchResults);

		etFriendName = (EditText)root.findViewById(R.id.etFriendName);
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvSearchResults.setAdapter(null);

				int id = getActivity().getIntent().getIntExtra(INTENT_KEY_USERID, NULL_USER);

				RequestParams params = new RequestParams();
				params.put("search", e.toString());
				params.put("user_id", Integer.toString(id));

				AsyncHttpClient client = new AsyncHttpClient();
				client.get("http://www.lol-fc.com/classmate/searchfriends.php", params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						List<Friend> search_results = new ArrayList<Friend>();
						if (1 < response.length()) {
							try {
								JSONObject jObj;
								JSONArray myjsonarray = new JSONArray(response);
								for (int i = 0; i < myjsonarray.length(); i++) {
									jObj = myjsonarray.getJSONObject(i);
									search_results.add(new Friend(jObj.getInt("user_id"), jObj.getString("username"), jObj.getString("email")));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						AddFriendAdapter adapter = new AddFriendAdapter(getActivity(), search_results);
						lvSearchResults.setAdapter(adapter);
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

		return root;
	}
}