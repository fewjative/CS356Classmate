package edu.csupomona.cs.cs356.classmate.fragments.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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
		etFriendName.requestFocus();
		etFriendName.addTextChangedListener(new TextWatcherAdapter() {
			@Override
			public void afterTextChanged(Editable e) {
				if (e.length() == 0) {
					return;
				}

				llProgressBar.setVisibility(View.VISIBLE);
				lvSearchResults.setAdapter(null);

				RequestParams params = new RequestParams();
				params.put("search", e.toString());

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

		return root;
	}
}