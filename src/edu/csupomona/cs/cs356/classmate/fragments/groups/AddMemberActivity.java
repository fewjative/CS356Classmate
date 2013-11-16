package edu.csupomona.cs.cs356.classmate.fragments.groups;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import edu.csupomona.cs.cs356.classmate.R;
import edu.csupomona.cs.cs356.classmate.abstractions.Friend;
import edu.csupomona.cs.cs356.classmate.abstractions.Group;
import static edu.csupomona.cs.cs356.classmate.fragments.GroupsFragment.INTENT_KEY_GROUP;
import edu.csupomona.cs.cs356.classmate.utils.TextWatcherAdapter;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddMemberActivity extends Activity {
	private EditText etFriendName;
	private ListView lvSearchResults;
	private LinearLayout llProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_member_activity);

		final Group g = getIntent().getParcelableExtra(INTENT_KEY_GROUP);

		llProgressBar = (LinearLayout)findViewById(R.id.llProgressBar);
		lvSearchResults = (ListView)findViewById(R.id.lvSearchResults);

		etFriendName = (EditText)findViewById(R.id.etFriendName);
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
				params.put("group_id", Integer.toString(g.getID()));

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
									search_results.add(new Friend(
										jObj.getInt("user_id"),
										jObj.getString("username"),
										jObj.getString("email")
									));
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						AddMemberAdapter adapter = new AddMemberAdapter(AddMemberActivity.this, g, search_results);
						lvSearchResults.setAdapter(adapter);
						llProgressBar.setVisibility(View.GONE);
					}
				});
			}
		});
	}
}